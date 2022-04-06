/*
 * Copyright 2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.update.spiget;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SpigetUpdateAbstract {

	public static final String RESOURCE_INFO    = "https://api.spiget.org/v2/resources/%s?ut=%s";
	public static final String RESOURCE_VERSION = "https://api.spiget.org/v2/resources/%s/versions/latest?ut=%s";

	protected final int    resourceId;
	protected final String currentVersion;
	protected final Logger log;
	protected String            userAgent         = "SpigetResourceUpdater";
	protected VersionComparator versionComparator = VersionComparator.EQUAL;

	protected ResourceInfo latestResourceInfo;

	public SpigetUpdateAbstract(int resourceId, String currentVersion, Logger log) {
		this.resourceId = resourceId;
		this.currentVersion = currentVersion;
		this.log = log;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setVersionComparator(VersionComparator comparator) {
		this.versionComparator = comparator;
	}

	public ResourceInfo getLatestResourceInfo() {
		return latestResourceInfo;
	}

	protected abstract void dispatch(Runnable runnable);

	public boolean isVersionNewer(String oldVersion, String newVersion) {
		return versionComparator.isNewer(oldVersion, newVersion);
	}

	public void checkForUpdate(final UpdateCallback callback) {
		dispatch(() -> {
			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(String.format(RESOURCE_INFO, resourceId, System.currentTimeMillis())).openConnection();
				connection.setRequestProperty("User-Agent", getUserAgent());
				JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
				latestResourceInfo = new Gson().fromJson(jsonObject, ResourceInfo.class);

				connection = (HttpURLConnection) new URL(String.format(RESOURCE_VERSION, resourceId, System.currentTimeMillis())).openConnection();
				connection.setRequestProperty("User-Agent", getUserAgent());
				jsonObject = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
				latestResourceInfo.latestVersion = new Gson().fromJson(jsonObject, ResourceVersion.class);

				if (isVersionNewer(currentVersion, latestResourceInfo.latestVersion.name)) {
					callback.updateAvailable(latestResourceInfo.latestVersion.name, "https://spigotmc.org/" + latestResourceInfo.file.url, !latestResourceInfo.external&&!latestResourceInfo.premium);
				} else {
					callback.upToDate();
				}
			} catch (Exception e) {
				log.log(Level.WARNING, "Failed to get resource info from spiget.org", e);
			}
		});
	}

}
