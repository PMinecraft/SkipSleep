# SkipSleep

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.13+-green.svg" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/Spigot-API-orange.svg" alt="Spigot API">
  <img src="https://img.shields.io/badge/Java-17+-blue.svg" alt="Java Version">
  <img src="https://img.shields.io/badge/Version-2.0.0-brightgreen.svg" alt="Plugin Version">
</p>

A lightweight Minecraft Spigot plugin that allows skipping the night with only a portion of players sleeping. Perfect for multiplayer servers where gathering all players to sleep simultaneously is impractical.

## ✨ Features

- **Flexible Skip Modes**
  - **Number Mode**: Skip night when a fixed number of players sleep
  - **Percentage Mode**: Skip night when a percentage of players are sleeping

- **AFK Detection**
  - Built-in AFK tracking (movement, chat, interaction, attack)
  - Configurable inactivity threshold
  - AFK players excluded from sleep count

- **Multi-World Support**
  - Independent sleep tracking per world
  - Configurable world exclusion list
  - World-specific announcements

- **Multi-Language Support**
  - Chinese (中文)
  - English
  - German (Deutsch)
  - Custom language file support

- **Sound Notifications**
  - Configurable notification sounds
  - Adjustable volume and pitch

- **Auto-Update Checker**
  - Optional update notifications on startup
  - Admin notifications on join

## 📦 Installation

1. Download the latest release from [SpigotMC](https://www.spigotmc.org/resources/skipsleep.106146/) or [GitHub Releases](https://github.com/plaidmrdeer/SkipSleep/releases)
2. Place `SkipSleep.jar` in your server's `plugins` folder
3. Restart or reload your server
4. Edit `plugins/SkipSleep/config.yml` to customize settings

## ⚙️ Configuration

```yaml
# Enable/disable skip sleep feature
skip: true

# Skip mode: 'num' (fixed number) or 'pet' (percentage)
model: 'num'

# Number mode: players needed to sleep
skipNum: 1

# Percentage mode: percentage of players needed (1-100)
skipPet: 50

# Sound settings
sound:
  enabled: true
  name: 'BLOCK_NOTE_BLOCK_BASS'
  volume: 1.0
  pitch: 1.0

# AFK detection settings
afk:
  excludeAfk: false
  inactivityThreshold: 300  # seconds

# Disabled worlds list
disabledWorlds: []

# Message display settings
showProgressMessages: true
showSkipMessage: true

# Language: chinese, english, german
Language:
  lang: english
```

## 🔧 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sks enable <on/off>` | Enable/disable skip feature | `skipsleep.command.enable` |
| `/sks number <num>` | Set required player count | `skipsleep.command.number` |
| `/sks mode <num/pet>` | Switch between modes | `skipsleep.command.mode` |
| `/sks percent <percent>` | Set required percentage | `skipsleep.command.percent` |
| `/sks sound <on/off>` | Toggle notification sounds | `skipsleep.command.sound` |
| `/sks status` | View current status | `skipsleep.command.status` |
| `/sks update <on/off>` | Toggle update checking | `skipsleep.command.update` |
| `/sks reload` | Reload configuration | `skipsleep.command.reload` |
| `/sks help` | Show help message | `skipsleep.command.help` |

> **Alias**: Use `/skipsleep` or `/sks`

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `skipsleep.command.*` | All SkipSleep commands | OP |
| `skipsleep.command.enable` | Enable/disable feature | OP |
| `skipsleep.command.number` | Set player count | OP |
| `skipsleep.command.mode` | Switch modes | OP |
| `skipsleep.command.percent` | Set percentage | OP |
| `skipsleep.command.sound` | Toggle sounds | OP |
| `skipsleep.command.status` | View status | OP |
| `skipsleep.command.reload` | Reload config | OP |
| `skipsleep.command.update` | Toggle updates | OP |
| `skipsleep.command.help` | View help | OP |

## 🌐 Custom Language

Create your own language file in `plugins/SkipSleep/lang/` folder:

1. Copy an existing language file (e.g., `english.yml`)
2. Rename it (e.g., `mylang.yml`)
3. Translate the messages
4. Set `Language.lang: mylang` in `config.yml`

## 🏗️ Building from Source

### Requirements
- Java 17+
- Maven 3.6+

### Build
```bash
git clone https://github.com/plaidmrdeer/SkipSleep.git
cd SkipSleep
mvn clean package
```

The compiled JAR will be in `target/SkipSleep-2.0.0.jar`

## 📊 Statistics

This plugin uses [bStats](https://bstats.org/) to collect anonymous usage statistics.

## 🔗 Links

- [SpigotMC Resource Page](https://www.spigotmc.org/resources/skipsleep.106146/)
- [GitHub Repository](https://github.com/plaidmrdeer/SkipSleep)
- [Bug Reports & Issues](https://github.com/plaidmrdeer/SkipSleep/issues)

## 📄 License

This project is open source. Feel free to use, modify, and distribute.

## 👨‍💻 Author

**PlaidMrdeer**

---

<p align="center">
  Made with ❤️ for the Minecraft community
</p>
