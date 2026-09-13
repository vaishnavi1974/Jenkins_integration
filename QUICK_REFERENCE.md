# Quick Reference: Fixing Jenkins + Docker Test Failures

## What Was Wrong

Your tests were failing because:
1. ❌ **Hardcoded IP Address**: Tests pointed to `192.168.29.4:4444` (your local machine IP)
   - This doesn't work in Docker containers or on different machines
   
2. ❌ **No Selenium Grid Setup**: No docker-compose configuration for Selenium Grid
   
3. ❌ **Missing Dependencies**: Selenium libraries not explicitly included in pom.xml
   
4. ❌ **No Docker Integration in Jenkins**: No Jenkinsfile or Docker instructions for CI/CD

## What Was Fixed

### 1. **Dynamic Grid URL Configuration**
- ✅ Created `config.properties` file with multiple URLs:
  - `http://localhost:4444/wd/hub` - Local development
  - `http://selenium-hub:4444/wd/hub` - Docker Compose
  - `http://host.docker.internal:4444/wd/hub` - Jenkins Docker
  
- ✅ Updated `Login.java` to read from config:
  ```java
  driver=new RemoteWebDriver(new URL(getGridURL()), dc);
  ```

### 2. **Docker Compose for Selenium Grid**
- ✅ Created `docker-compose.yml` with:
  - Selenium Hub (4444)
  - Chrome Node (VNC: 6900)
  - Firefox Node (VNC: 6901)

### 3. **Added Selenium Dependencies**
- ✅ Updated `pom.xml` with:
  - `selenium-java:4.25.0`
  - `selenium-remote-driver:4.25.0`

### 4. **Jenkins Pipeline Setup**
- ✅ Created `Jenkinsfile` with:
  - Git checkout from GitHub
  - Docker Compose startup
  - Maven test execution
  - Test report publishing
  - Automatic cleanup

## How to Use

### Local Development (Test on Your Machine)

```bash
# 1. Start Selenium Grid in Docker
docker-compose up -d

# 2. Run tests
mvn clean test -Dselenium.grid.env=docker

# 3. View results
# Reports: test-output/emailable-report.html
# Surefire: target/surefire-reports/

# 4. Stop Selenium Grid
docker-compose down
```

### Jenkins CI/CD (Automated)

```bash
# 1. Push code to GitHub
git add .
git commit -m "Docker Selenium Grid integration"
git push origin main

# 2. In Jenkins:
#    - Create new Pipeline job
#    - Point to GitHub repo
#    - Set Script Path to: Jenkinsfile
#    - Enable GitHub webhook
#    - Tests run automatically on every push
```

### Manual Testing with Chrome/Firefox

```bash
# Option 1: Using specific environment via Maven
mvn test -Dselenium.grid.env=docker

# Option 2: Override in command line
mvn test -Dselenium.grid.env=ci

# Option 3: Run specific test class
mvn test -Dtest=Orange_HRM.Login -Dselenium.grid.env=docker
```

## Debugging Test Failures

### Check Selenium Grid Status
```bash
# Test if Grid is running
curl http://localhost:4444/wd/hub/status

# View Grid UI
# Open browser: http://localhost:4444
```

### View Container Logs
```bash
# View hub logs
docker-compose logs selenium-hub

# View chrome node logs
docker-compose logs chrome

# View firefox node logs
docker-compose logs firefox

# View all logs
docker-compose logs
```

### Debug with VNC (Visual Debugging)
```bash
# Chrome node VNC: localhost:6900
# Firefox node VNC: localhost:6901
# Password: (just press enter)

# Use VNC viewer to watch tests execute in real-time
```

### Restart Services
```bash
# Soft restart
docker-compose restart

# Hard restart (clean)
docker-compose down -v
docker-compose up -d
```

## Configuration Options

### Environment Variable for Grid URL
```bash
# In config.properties
selenium.grid.env=docker    # Options: local, docker, ci

# Or via Maven command
mvn test -Dselenium.grid.env=docker
mvn test -Dselenium.grid.env=local
mvn test -Dselenium.grid.env=ci
```

### Parallel Test Execution
Edit `testng.xml`:
```xml
<suite name="Suite" parallel="tests" thread-count="5">
```

### Change Thread Count
Edit `docker-compose.yml`:
```yaml
environment:
  - GRID_MAX_SESSION=10    # Max sessions
  - SE_NODE_MAX_SESSIONS=5  # Sessions per node
```

## Common Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| Connection refused | Grid not running | `docker-compose up -d` |
| Grid URL not reachable | Wrong hostname in Docker | Use `selenium-hub` in docker-compose |
| Tests pass locally, fail in Jenkins | IP address changes | Use config.properties instead of hardcoded URL |
| Timeout waiting for element | Grid overloaded | Reduce thread-count or increase timeout |
| Docker network error | Network issue | Run `docker network prune` then restart |
| Port 4444 already in use | Another service using port | `docker-compose down` first |

## File Structure

```
Jenkins_Integration/
├── src/
│   ├── test/
│   │   ├── java/Orange_HRM/
│   │   │   └── Login.java (UPDATED: Uses config.properties)
│   │   └── resources/
│   │       └── config.properties (NEW: Grid URL configuration)
├── pom.xml (UPDATED: Added Selenium dependencies)
├── testng.xml
├── docker-compose.yml (NEW: Selenium Grid setup)
├── Jenkinsfile (NEW: CI/CD pipeline)
├── .gitignore (NEW: Git ignore patterns)
├── DOCKER_JENKINS_SETUP.md (NEW: Full setup guide)
└── QUICK_REFERENCE.md (NEW: This file)
```

## Next Steps

1. ✅ Commit all changes: `git add . && git commit -m "Docker Selenium integration"`
2. ✅ Push to GitHub: `git push origin main`
3. ✅ Create Jenkins Pipeline job pointing to your repo
4. ✅ Enable GitHub webhook in repository settings
5. ✅ Run first test manually: `mvn test`
6. ✅ Verify test output: `test-output/emailable-report.html`
7. ✅ Trigger Jenkins job from GitHub push

## Support Commands

```bash
# Test connection to Grid
curl -X POST http://localhost:4444/wd/hub/session \
  -H "Content-Type: application/json" \
  -d '{"desiredCapabilities":{"browserName":"chrome"}}'

# Check Docker status
docker ps                  # Running containers
docker-compose ps          # Compose services
docker logs container_id   # View logs

# Clean everything
docker system prune        # Remove unused resources
docker-compose down -v     # Remove volumes too
```

## Reference Documentation

- 📚 [Selenium Grid 4 Docs](https://www.selenium.dev/documentation/grid/)
- 📚 [Docker Compose Docs](https://docs.docker.com/compose/)
- 📚 [TestNG Configuration](https://testng.org/doc/documentation-main.html)
- 📚 [Jenkins Pipeline](https://www.jenkins.io/doc/book/pipeline/)

---
**Last Updated**: September 2026
**Status**: ✅ All fixes applied
