# Jenkins Integration with Docker & Selenium Grid

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](#)
[![Tests](https://img.shields.io/badge/tests-automated-blue)](#)
[![Docker](https://img.shields.io/badge/docker-ready-0db7ed)](#)
[![Java](https://img.shields.io/badge/java-17-orange)](#)
[![License](https://img.shields.io/badge/license-MIT-green)](#)

> Automated UI testing with Jenkins, Docker, Selenium Grid, and TestNG

## 🎯 What This Project Does

This project demonstrates production-ready automation testing setup with:
- **Selenium Grid** in Docker for cross-browser testing
- **Jenkins** pipeline for CI/CD automation
- **GitHub** integration for automated workflows
- **TestNG** for test execution and reporting

## ✨ Key Features

- ✅ **Works Anywhere**: Local machine, Docker container, or Jenkins server
- ✅ **Automated**: Tests run automatically on every GitHub push
- ✅ **Multi-Browser**: Chrome and Firefox nodes
- ✅ **Debuggable**: VNC access for live test debugging
- ✅ **Scalable**: Easy to add more browsers or parallel execution
- ✅ **Well-Documented**: Complete guides and quick references included

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Maven 3.8.0+
- Java 17+

### Local Testing (2 commands)
```bash
# Start Selenium Grid
docker-compose up -d

# Run tests
mvn clean test -Dselenium.grid.env=docker
```

View results: `test-output/emailable-report.html`

### Automated Testing (Jenkins)
```bash
# Push to GitHub
git add . && git commit -m "Setup" && git push

# In Jenkins:
# 1. New Pipeline Job
# 2. Git repo URL
# 3. Script path: Jenkinsfile
# 4. Build → Tests run automatically!
```

## 📁 Project Structure

```
├── src/test/java/Orange_HRM/
│   └── Login.java              # Test class (dynamic Grid URL)
├── src/test/resources/
│   └── config.properties       # Grid URL configuration
├── docker-compose.yml          # Selenium Grid setup
├── Jenkinsfile                 # CI/CD pipeline
└── pom.xml                     # Maven dependencies
```

## 📚 Documentation

| Guide | Purpose |
|-------|---------|
| [SOLUTION_SUMMARY.md](SOLUTION_SUMMARY.md) | **START HERE** - Complete overview |
| [QUICK_REFERENCE.md](QUICK_REFERENCE.md) | Commands and troubleshooting |
| [DOCKER_JENKINS_SETUP.md](DOCKER_JENKINS_SETUP.md) | Detailed setup instructions |
| [IMPLEMENTATION_CHECKLIST.md](IMPLEMENTATION_CHECKLIST.md) | Verification checklist |
| [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) | Architecture overview |

## 🔧 Configuration

### Change Selenium Grid Environment
Edit `src/test/resources/config.properties`:
```properties
selenium.grid.env=docker    # Options: local, docker, ci
```

Or via Maven:
```bash
mvn test -Dselenium.grid.env=docker
```

### Add More Browsers
Edit `docker-compose.yml` and duplicate a node section.

### Parallel Execution
Edit `testng.xml`:
```xml
<suite parallel="tests" thread-count="5">
```

## 🐳 Docker Services

```
Selenium Hub:    http://localhost:4444
Chrome Node:     VNC @ localhost:6900
Firefox Node:    VNC @ localhost:6901
```

## 🚀 Jenkins Pipeline

Automatically:
1. Clones from GitHub
2. Starts Selenium Grid
3. Runs Maven tests
4. Publishes reports
5. Cleans up containers

Triggered by GitHub push (webhook configured).

## 📊 Test Reports

- **Location**: `test-output/emailable-report.html`
- **Surefire**: `target/surefire-reports/`
- **Jenkins**: Published in job console

## 🆘 Troubleshooting

### Tests fail locally
```bash
docker-compose logs selenium-hub
```

### Jenkins can't reach Selenium Grid
Check `config.properties` - ensure correct URL for your Jenkins setup

### Port 4444 in use
```bash
docker-compose down -v
docker-compose up -d
```

More: See [QUICK_REFERENCE.md](QUICK_REFERENCE.md)

## 🎯 Common Commands

```bash
# Start Grid & run tests
docker-compose up -d && mvn test && docker-compose down

# Run specific test class
mvn test -Dtest=Orange_HRM.Login

# Run with verbose output
mvn test -X

# View Grid UI
open http://localhost:4444

# Connect VNC for debugging
# Chrome: vnc://localhost:6900
# Firefox: vnc://localhost:6901
```

## 📈 Pipeline Status

| Step | Status |
|------|--------|
| Code checkout | ✅ Working |
| Docker setup | ✅ Working |
| Maven build | ✅ Working |
| Test execution | ✅ Working |
| Report generation | ✅ Working |
| Cleanup | ✅ Working |

## 🎓 Technologies Used

- **Java 17** - Programming language
- **Selenium 4.25** - Browser automation
- **TestNG 7.8** - Test framework
- **Maven 3.8** - Build tool
- **Docker & Docker Compose** - Containerization
- **Jenkins** - CI/CD platform
- **GitHub** - Version control & automation

## 📝 Test Cases

- **Orange_HRM.Login** - Login test with Chrome & Firefox
- **DataProvider** - Data-driven test with multiple credentials
- **Parallel Execution** - Tests run simultaneously

## 🔒 Security

- No hardcoded credentials in code
- Configuration-based settings
- Environment-specific URLs
- Clean .gitignore

## 🚀 Next Steps

1. **Try locally**: `docker-compose up -d && mvn test`
2. **Push to GitHub**: `git add . && git push`
3. **Create Jenkins job**: Point to your GitHub repo
4. **Enable webhook**: GitHub → Settings → Webhooks
5. **Enjoy automation!** Tests run on every push

## 📖 Detailed Guides

- [Setup Guide](DOCKER_JENKINS_SETUP.md) - 300+ lines of detailed instructions
- [Quick Reference](QUICK_REFERENCE.md) - Commands and solutions
- [Architecture](PROJECT_STRUCTURE.md) - System design
- [Checklist](IMPLEMENTATION_CHECKLIST.md) - Verification steps

## 🤝 Contributing

Feel free to extend this project:
- Add more test cases
- Add more browsers to docker-compose.yml
- Integrate with reporting tools
- Add more Jenkins pipeline stages

## 📞 Support

1. Check [QUICK_REFERENCE.md](QUICK_REFERENCE.md) for common issues
2. Review [DOCKER_JENKINS_SETUP.md](DOCKER_JENKINS_SETUP.md) for troubleshooting
3. Check Docker logs: `docker-compose logs`

## ✅ Checklist for First Run

- [ ] Docker installed and running
- [ ] Maven installed (mvn --version)
- [ ] Java 17+ installed (java --version)
- [ ] Git configured
- [ ] Code cloned/pulled
- [ ] Read SOLUTION_SUMMARY.md
- [ ] Ran: `docker-compose up -d`
- [ ] Ran: `mvn clean test`
- [ ] Checked: test-output/emailable-report.html
- [ ] Ran: `docker-compose down`
- [ ] Pushed to GitHub
- [ ] Created Jenkins job
- [ ] Enabled webhook
- [ ] Triggered first build
- [ ] ✅ Tests running automatically!

## 📄 License

MIT License - Free to use and modify

## 🎉 Ready to Go!

Everything is set up and documented. Follow the Quick Start above to get running in minutes.

**Questions?** Check the documentation files or review the inline code comments.

Happy Testing! 🚀

---

**Last Updated**: September 2026  
**Status**: ✅ Production Ready  
**Version**: 1.0
