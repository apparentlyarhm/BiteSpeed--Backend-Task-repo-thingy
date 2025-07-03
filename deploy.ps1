# THIS SCRIPT HAS TO BE ADAPTED TO BASH FOR GITHUB ACTIONS BASED PIPELINES

function Check-EmptyVars {
    param (
        [string[]]$vars
    )
    foreach ($var in $vars) {
        if ([string]::IsNullOrWhiteSpace((Get-Variable -Name $var -ValueOnly))) {
            Write-Host "ERROR: Environment variable '$var' is missing or empty!" -ForegroundColor Red
            exit 1
        }
    }
}

Write-Host "Local CI/CD (sort of) for testing..." -ForegroundColor Cyan

# docker, gh cli and gcloud cli is required
docker info *> $null
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Docker is not running. Please start Docker and try again." -ForegroundColor Red
    exit 1
}

if (-not (Get-Command gcloud -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: Google Cloud CLI (gcloud) is not installed. Please install it and try again." -ForegroundColor Red
    exit 1
}

Get-Content .env | ForEach-Object {
    if ($_ -match "^(.*)=(.*)$") {
        $envName = $matches[1].Trim()
        $envValue = $matches[2].Trim()
        [System.Environment]::SetEnvironmentVariable($envName, $envValue, "Process")
    }
}

$GOOGLE_CLOUD_PROJECT = $env:GOOGLE_CLOUD_PROJECT
$GOOGLE_CLOUD_REGION = $env:GOOGLE_CLOUD_REGION
$GOOGLE_CLOUD_AR_REPO_NAME = $env:GOOGLE_CLOUD_AR_REPO_NAME
$GOOGLE_CLOUD_CR_SERVICE_NAME=$env:GOOGLE_CLOUD_CR_SERVICE_NAME
$DB_NAME=$env:DB_NAME
$DB_INSTANCE_NAME=$env:DB_INSTANCE_NAME
$MYSQL_USER=$env:MYSQL_USER
$MYSQL_PASS=$env:MYSQL_PASS

Check-EmptyVars @(
    "GOOGLE_CLOUD_PROJECT"
    "GOOGLE_CLOUD_REGION"
    "GOOGLE_CLOUD_AR_REPO_NAME"
    "GOOGLE_CLOUD_CR_SERVICE_NAME"
    "DB_NAME"
    "DB_INSTANCE_NAME"
    "MYSQL_USER"
    "MYSQL_PASS"
)

Write-Host "All required environment variables are set!" -ForegroundColor Green

$SERVICE_ACCOUNT = "bitespeed-project-withsql@$GOOGLE_CLOUD_PROJECT.iam.gserviceaccount.com"
Write-Host "Using Service Account: $SERVICE_ACCOUNT"

$IMAGE_NAME = "$GOOGLE_CLOUD_REGION-docker.pkg.dev/$GOOGLE_CLOUD_PROJECT/$GOOGLE_CLOUD_AR_REPO_NAME/$GOOGLE_CLOUD_CR_SERVICE_NAME`:latest"
Write-Host "Using Image Name: $IMAGE_NAME"

Write-Host "Authenticating with Google Cloud..."

Write-Host "skipping gcloud auth login.."
#gcloud auth login
gcloud auth configure-docker $GOOGLE_CLOUD_REGION-docker.pkg.dev

# Currently in my local the authentication is done properly, so no need. keeping it here for reference
#Write-Host "Setting GCP Project..."
#gcloud config set project $PROJECT_ID

docker build `
 --build-arg DB_NAME="$DB_NAME" `
 --build-arg DB_INSTANCE_NAME="$DB_INSTANCE_NAME" `
 --build-arg MYSQL_USER="$MYSQL_USER" `
 --build-arg MYSQL_PASS="$MYSQL_PASS" `
 -t "$IMAGE_NAME" .

Write-Host "Pushing image.."
docker push $IMAGE_NAME

# Check if the service account exists
$saExists = gcloud iam service-accounts list --project=$GOOGLE_CLOUD_PROJECT --format="value(email)" | Select-String -Pattern $SERVICE_ACCOUNT

if (-not $saExists) {
    Write-Host "❌ ERROR: Service account $SERVICE_ACCOUNT does not exist. To setup ADC, a service account with permissions is required." -ForegroundColor Red
    Exit 1
}

Write-Host "Deploying to Cloud Run..."
gcloud run deploy $GOOGLE_CLOUD_CR_SERVICE_NAME `
  --image=$IMAGE_NAME `
  --platform=managed `
  --service-account=$SERVICE_ACCOUNT `
  --region=$GOOGLE_CLOUD_REGION `
  --add-cloudsql-instances=$DB_INSTANCE_NAME `
  --allow-unauthenticated

Write-Host "Deployment completed!"

