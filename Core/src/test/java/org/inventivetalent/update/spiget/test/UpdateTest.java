package org.inventivetalent.update.spiget.test;

import org.inventivetalent.update.spiget.SpigetUpdateAbstract;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class UpdateTest {

	@Test
	public void updateTest() {
		SpigetUpdateAbstract updateCheck = new SpigetUpdateAbstract(5341/* NickNamer */, "1.0.0", Logger.getLogger("UpdateTest")) {
			@Override
			protected void dispatch(Runnable runnable) {
				runnable.run();
			}
		};
		updateCheck.checkForUpdate(new UpdateCallback() {
			@Override
			public void updateAvailable(String newVersion, String downloadUrl, boolean canAutoDownload) {
				System.out.println("newVersion = [" + newVersion + "], downloadUrl = [" + downloadUrl + "], canAutoDownload = [" + canAutoDownload + "]");

				assertTrue(downloadUrl.startsWith("https://spigotmc.org/resources/nicknamer-integrated-api.5341/download"));
				assertFalse(canAutoDownload);
			}

			@Override
			public void upToDate() {
				System.out.println("up-to-date");

				fail("Resource is up-to-date, but should have an update");
			}
		});
	}

}
