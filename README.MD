![Build Status](http://ci.inventivetalent.org/job/SpigetResourceUpdater/badge/icon)


Spigot & Bungeecord updater API which uses [spiget.org](https://spiget.org) to check for new versions

## Usage
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