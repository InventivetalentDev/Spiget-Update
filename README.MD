[![Build Status](https://travis-ci.org/InventivetalentDev/SpigetUpdater.svg?branch=master)](https://travis-ci.org/InventivetalentDev/SpigetUpdater)
[![GitHub release](https://img.shields.io/github/release/InventivetalentDev/SpigetUpdater.svg)](https://github.com/InventivetalentDev/SpigetUpdater)


Spigot & Bungeecord updater API which uses [spiget.org](https://spiget.org) to check for new versions

### Simply Ballistic's fork
#### Useful features added such as: 
- Get update info such as name and likes
- disable logging with a boolean value 
- optimisation 
- Run code on fail checking for updates
- disable updating by adding -DEV tag to version 

All changes are on Bukkit only!

You are welcome to use this fork! Remember to mention the respectful owners. 

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
new SpigetUpdate(this, 12345)
.checkForUpdate(new UpdateCallback() {
	@Override
	public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload,SpigetUpdateAbstract updater) {
		// First check if there is a direct download available
		// (Either the resources is hosted on spigotmc.org, or Spiget has a cached version to download)
		// external downloads won't work if they are disabled (by default) in spiget.properties
		if (hasDirectDownload) {
			if (updater.downloadUpdate()) {
				// Update downloaded, will be loaded when the server restarts
				getLogger.info("This plugin was updated!");
			} else {
				// Update failed
				getLogger().warning("Update download failed, reason is " + updater.getFailReason());
			}
		}
	}

	@Override
	public void upToDate(SpigetUpdateAbstract updater) {
	getLogger.info("This plugin is up to date!");
	}
	@Override
         public void failedCheck(Exception ex, SpigetUpdateAbstract updater) {
                getLogger.info("The updater failed! Is your internet working?");
            }
});
```

## Maven
```xml
 <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
</repositories>
<dependencies>
     <dependency>
        <groupId>com.github.SimplyBallistic</groupId>
        <artifactId>Spiget-Update</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```
