[![Build Status](https://travis-ci.org/InventivetalentDev/SpigetUpdater.svg?branch=master)](https://travis-ci.org/InventivetalentDev/SpigetUpdater)
[![GitHub release](https://img.shields.io/github/release/InventivetalentDev/SpigetUpdater.svg)](https://github.com/InventivetalentDev/SpigetUpdater)


Spigot & Bungeecord updater API which uses [spiget.org](https://spiget.org) to check for new versions

### Simply Ballistic's fork
#### Useful features added such as: 
- Get update info such as name and likes
- disable logging with a boolean value 
- optimisation 

## Usage

### Check for new versions

```Java
SpigetUpdate updater = new SpigetUpdate(this, 12345);

// This compares versions just by checking if they are equal
// This means that older versions will also be treated as updates
updater.setVersionComparator(VersionComparator.EQUAL);

// This converts a semantic version to an integer and checks if the updated version is greater
updater.setVersionComparator(VersionComparator.SEM_VER);

updater.checkForUpdate(new UpdateCallback() {
	@Override
	public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
		//// A new version is available
		// newVersion - the latest version
		// downloadUrl - URL to the download
		// hasDirectDownload - whether the update is available for a direct download on spiget.org
	}

	@Override
	public void upToDate() {
		//// Plugin is up-to-date
	}
});
```


### Download updates (Bukkit/Spigot)
```Java
final SpigetUpdate updater = new SpigetUpdate(this, 12345);
updater.checkForUpdate(new UpdateCallback() {
	@Override
	public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
		// First check if there is a direct download available
		// (Either the resources is hosted on spigotmc.org, or Spiget has a cached version to download)
		// external downloads won't work if they are disabled (by default) in spiget.properties
		if (hasDirectDownload) {
			if (updater.downloadUpdate()) {
				// Update downloaded, will be loaded when the server restarts
			} else {
				// Update failed
				getLogger().warning("Update download failed, reason is " + updater.getFailReason());
			}
		}
	}

	@Override
	public void upToDate() {
	}
});
```

## Maven
```xml
<repositories>
    <repository>
        <id>inventive-repo</id>
        <url>https://repo.inventivetalent.org/content/groups/public/</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>org.inventivetalent.spiget-update</groupId>
        <artifactId>bukkit|bungee</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```
