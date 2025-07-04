package com.bitespeed.coolname.service.impl;

import com.bitespeed.coolname.entity.Contact;
import com.bitespeed.coolname.exception.BaseException;
import com.bitespeed.coolname.exception.RestrictedException;
import com.bitespeed.coolname.model.ContactResponse;
import com.bitespeed.coolname.model.IdentifyRequest;
import com.bitespeed.coolname.model.IdentifyResponse;
import com.bitespeed.coolname.model.enums.LinkPrecedence;
import com.bitespeed.coolname.repository.ContactRepo;
import com.bitespeed.coolname.service.contract.IdentityService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class IdentifyServiceImpl implements IdentityService {

    private final LocalTime blockStart = LocalTime.of(22, 0); // 10 PM
    private final LocalTime blockEnd = LocalTime.of(7, 0);    // 7 AM

    @Autowired
    private ContactRepo contactRepo;

    @Override
    @Transactional
    public IdentifyResponse identifyContact(IdentifyRequest request) throws BaseException {
        LocalTime now = LocalTime.now();
        boolean isBlocked = now.isAfter(blockStart) || now.isBefore(blockEnd);

        if (isBlocked) {
            throw new RestrictedException(111, "Access is not allowed between 10 PM and 7 AM, i am trying to save costs here so i just close the database ");
        }

        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        List<Contact> matchingContacts = contactRepo.findByEmailOrPhoneNumberOrderByCreatedAtAsc(email, phoneNumber);
        assert matchingContacts.size() <= 2; // If assertion fails something is wrong

        if (matchingContacts.isEmpty()) {
            Contact newContact = createNewContact(email, phoneNumber, null, LinkPrecedence.PRIMARY);
            return buildResponse(newContact);

        } else {
            Contact primaryContact = matchingContacts.get(0);
            String primaryContactUuid = primaryContact.getContactId();

            for (int i = 1; i < matchingContacts.size(); i++) {
                Contact currentContact = matchingContacts.get(i);
                if (currentContact.getLinkPrecedence() == LinkPrecedence.PRIMARY) {
                    currentContact.setLinkedId(primaryContactUuid);
                    currentContact.setLinkPrecedence(LinkPrecedence.SECONDARY);

                    contactRepo.save(currentContact);
                }
            }

            boolean newInfoExists = true;
            if (email != null && phoneNumber != null) {
                newInfoExists = matchingContacts.stream()
                        .noneMatch(c -> email.equals(c.getEmail()) && phoneNumber.equals(c.getPhoneNumber()));
            }

            if (newInfoExists) {
                createNewContact(email, phoneNumber, primaryContactUuid, LinkPrecedence.SECONDARY);
            }

            // Re-fetch all related contacts using the new repository method and UUID
            List<Contact> allRelatedContacts = contactRepo.findByContactIdOrLinkedId(primaryContactUuid, primaryContactUuid);
            return buildResponse(allRelatedContacts);
        }
    }

    private Contact createNewContact(String email, String phoneNumber, String linkedId, LinkPrecedence precedence) {
        Contact contact = new Contact();

        contact.setEmail(email);
        contact.setPhoneNumber(phoneNumber);
        contact.setLinkedId(linkedId);
        contact.setLinkPrecedence(precedence);

        return contactRepo.save(contact);
    }

    private IdentifyResponse buildResponse(Contact primary) {
        ContactResponse contactResponse = new ContactResponse(
                primary.getContactId(),
                Collections.singletonList(primary.getEmail()),
                Collections.singletonList(primary.getPhoneNumber()),
                new ArrayList<>()
        );

        return new IdentifyResponse(contactResponse);
    }

    private IdentifyResponse buildResponse(List<Contact> contacts) {
        Contact primaryContact = contacts.stream()
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .findFirst()
                .orElse(contacts.get(0));

        Set<String> emails = new LinkedHashSet<>();
        Set<String> phoneNumbers = new LinkedHashSet<>();
        List<String> secondaryContactIds = new ArrayList<>();

        if (primaryContact.getEmail() != null) emails.add(primaryContact.getEmail());
        if (primaryContact.getPhoneNumber() != null) phoneNumbers.add(primaryContact.getPhoneNumber());

        for (Contact contact : contacts) {
            if (contact.getEmail() != null) emails.add(contact.getEmail());
            if (contact.getPhoneNumber() != null) phoneNumbers.add(contact.getPhoneNumber());
            if (contact.getLinkPrecedence() == LinkPrecedence.SECONDARY) {
                secondaryContactIds.add(contact.getContactId());
            }
        }

        ContactResponse contactResponse = new ContactResponse(
                primaryContact.getContactId(),
                new ArrayList<>(emails),
                new ArrayList<>(phoneNumbers),
                secondaryContactIds
        );
        return new IdentifyResponse(contactResponse);
    }
}
