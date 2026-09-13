# Jenkins Integration with Docker & Selenium Grid

## Overview
This project is configured to run TestNG tests with Selenium Grid in a Dockerized environment via Jenkins CI/CD pipeline.

## Prerequisites

### Local Development
- Docker & Docker Compose installed
- Maven 3.8.0+
- Java 17+
- Jenkins (for CI/CD)

### Jenkins Requirements
- Jenkins with Docker plugin installed
- Docker and Docker Compose available on Jenkins agent
- Git plugin (already included)

## Setup Instructions

### 1. Local Development (Manual Testing with Docker)

```bash
# Navigate to project directory
cd Jenkins_Integration

# Start Selenium Grid with Docker Compose
docker-compose up -d

# Run tests pointing to Docker Selenium Grid
mvn clean test -Dselenium.grid.env=docker

# Stop Selenium Grid
docker-compose down
```

### 2. Jenkins Pipeline Setup

#### Step 1: Configure Jenkins
1. Go to Jenkins Dashboard
2. Manage Jenkins → Manage Plugins
3. Install plugins:
   - Docker plugin
   - Docker Pipeline plugin
   - HTML Publisher plugin
   - TestNG Results Plugin (optional)

#### Step 2: Configure Jenkins Agent for Docker
1. Ensure Docker is installed and running on Jenkins agent/master
2. Add Jenkins user to docker group (on Linux):
   ```bash
   sudo usermod -aG docker jenkins
   sudo systemctl restart jenkins
   ```

#### Step 3: Create Jenkins Pipeline Job
1. New Item → Pipeline
2. Name: `Jenkins-Integration-Tests`
3. Pipeline → Definition: Pipeline script from SCM
4. SCM: Git
5. Repository URL: `https://github.com/your-username/Jenkins_Integration.git`
6. Branch: `*/main` (or your default branch)
7. Script Path: `Jenkinsfile`
8. Save and Build

### 3. Docker Compose Configuration

The `docker-compose.yml` file sets up:
- **selenium-hub**: Central Selenium Grid hub on port 4444
- **chrome**: Chrome node with VNC access on port 6900
- **firefox**: Firefox node with VNC access on port 6901

#### Accessing VNC for Debugging:
- Chrome: `localhost:6900` (VNC viewer)
- Firefox: `localhost:6901` (VNC viewer)
- Password: (typically not set, just press enter)

### 4. Configuration

Edit `src/test/resources/config.properties` to choose environment:

```properties
# Options: local, docker, ci
selenium.grid.env=docker
```

Or pass via Maven command:
```bash
mvn test -Dselenium.grid.env=docker
```

### 5. Troubleshooting

#### Tests fail with connection refused
- Ensure docker-compose is running: `docker-compose ps`
- Check Selenium Grid is accessible: `curl http://localhost:4444/wd/hub/status`

#### Tests fail with "Grid URL not reachable"
- If running in Jenkins Docker container, use `http://host.docker.internal:4444/wd/hub`
- Ensure Docker network connectivity between containers

#### Docker network issues
```bash
# Clean up all Docker containers and networks
docker-compose down -v
docker network prune

# Restart services
docker-compose up -d
```

#### View Selenium Grid logs
```bash
docker-compose logs selenium-hub
docker-compose logs chrome
docker-compose logs firefox
```

### 6. Jenkins Docker Integration (Advanced)

If Jenkins runs in Docker, update Jenkinsfile:
```groovy
// Use host.docker.internal or expose host network
environment {
    SELENIUM_HUB_URL = "http://host.docker.internal:4444/wd/hub"
}
```

### 7. Parallel Test Execution

Update `testng.xml` for parallel execution:
```xml
<suite name="Suite" parallel="tests" thread-count="5">
    <test name="chromeTest">
        ...
    </test>
    <test name="firefoxTest">
        ...
    </test>
</suite>
```

## GitHub Integration

1. Push project to GitHub:
```bash
git init
git add .
git commit -m "Initial commit with Docker Selenium setup"
git branch -M main
git remote add origin https://github.com/your-username/Jenkins_Integration.git
git push -u origin main
```

2. Configure GitHub webhook in Jenkins:
   - GitHub Repository → Settings → Webhooks
   - Payload URL: `http://jenkins-server:8080/github-webhook/`
   - Events: Push events
   - Jenkins will auto-trigger on every push

## Test Execution Results

- TestNG Reports: `target/test-output/emailable-report.html`
- Surefire Reports: `target/surefire-reports/`
- Screenshots/Logs: `test-output/` directory

## Common Maven Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=Orange_HRM.Login

# Run with specific Selenium environment
mvn test -Dselenium.grid.env=docker

# Run with verbose output
mvn test -X

# Skip tests (build only)
mvn clean install -DskipTests

# Run with custom TestNG suite
mvn test -Dsuites=testng.xml
```

## Environment Variables

| Variable | Value | Purpose |
|----------|-------|---------|
| `selenium.grid.env` | `docker`, `local`, `ci` | Choose Selenium Grid environment |
| `DOCKER_NETWORK` | `jenkins-selenium` | Docker network name |
| `WORKSPACE_DIR` | Jenkins workspace | Project directory |

## CI/CD Best Practices

1. ✅ Run tests in isolated Docker containers
2. ✅ Use explicit URLs instead of hardcoded IPs
3. ✅ Generate and archive test reports
4. ✅ Implement retry logic for flaky tests
5. ✅ Monitor Selenium Grid health
6. ✅ Clean up resources after test runs
7. ✅ Use environment-specific configurations

## Support & Documentation

- [Selenium Grid Documentation](https://www.selenium.dev/documentation/grid/)
- [Docker Compose Docs](https://docs.docker.com/compose/)
- [TestNG Documentation](https://testng.org/doc/)
- [Jenkins Pipeline Docs](https://www.jenkins.io/doc/book/pipeline/)
