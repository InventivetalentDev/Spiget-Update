package org.inventivetalent.update.spiget.test;

import org.inventivetalent.update.spiget.SpigetUpdateAbstract;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.junit.Test;
import sun.security.provider.ConfigFile;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UpdateTest {

	@Test
	public void updateTest() {
		final SpigetUpdateAbstract updateCheck = new SpigetUpdateAbstract(5341/*NickNamer*/, "1.0.0", Logger.getLogger("UpdateTest")) {
			@Override
			protected void dispatch(Runnable runnable) {
				runnable.run();
			}
		};
		updateCheck.checkForUpdate(new UpdateCallback() {
			@Override
			public void updateAvailable(String newVersion, String downloadUrl, boolean canAutoDownload, SpigetUpdateAbstract updater) {
				System.out.println("newVersion = [" + newVersion + "],\n downloadUrl = [" + downloadUrl + "],\n canAutoDownload = [" + canAutoDownload + "], " +
                        "\nupdateTitle= ["+updateCheck.getLatestResourceInfo().latestVersion.updateInfo.title+"], updateLikes["+updater.getLatestResourceInfo().latestVersion.updateInfo.likes+"]");

				assertTrue(downloadUrl.startsWith("https://spigotmc.org/resources/nicknamer-integrated-api.5341/download"));
				assertFalse(canAutoDownload);

			}

			@Override
			public void upToDate(SpigetUpdateAbstract updater) {
				System.out.println("up-to-date");

				assertTrue("Resource is up-to-date, but should have an update", false);
			}

			@Override
			public void failedCheck(Exception ex,SpigetUpdateAbstract updater) {
				ex.printStackTrace();
				fail();
			}
		});
	}
    @Test
    public void shouldFail(){
	    SpigetUpdateAbstract spigetUpdateAbstract=new SpigetUpdateAbstract(-1,"1",Logger.getLogger("UpdateTest")) {
            @Override
            protected void dispatch(Runnable runnable) {
                runnable.run();
            }
        };
	    spigetUpdateAbstract.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl, boolean canAutoDownload, SpigetUpdateAbstract updater) {
                fail();
            }

            @Override
            public void upToDate(SpigetUpdateAbstract spigetUpdateAbstract) {
                fail();

            }

            @Override
            public void failedCheck(Exception ex,SpigetUpdateAbstract updater) {
                System.out.println("exception "+ ex.getClass().getSimpleName());
                assertTrue(ex instanceof FileNotFoundException);
            }
        });

    }
}
