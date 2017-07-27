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
import com.sun.istack.internal.Nullable;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SpigetUpdateAbstract {

	public static final String RESOURCE_INFO    = "http://api.spiget.org/v2/resources/%s?ut=%s";
	public static final String RESOURCE_VERSION = "http://api.spiget.org/v2/resources/%s/versions/latest?ut=%s";
	public static final String UPDATE_INFO = "http://api.spiget.org/v2/resources/%s/updates/latest?ut=%s";
	private final boolean canLog;

	protected final int    resourceId;
	protected final String currentVersion;
	protected final Logger log;
	protected String            userAgent         = "SpigetResourceUpdater";
	protected VersionComparator versionComparator = VersionComparator.EQUAL;

	protected ResourceInfo latestResourceInfo;

	public SpigetUpdateAbstract(int resourceId, String currentVersion, Logger log,boolean canLog) {
		this.resourceId = resourceId;
		this.currentVersion = currentVersion;
		this.log = log;
		this.canLog=canLog;
	}

	public SpigetUpdateAbstract(int resourceId, String currentVersion, Logger log) {
		this(resourceId,currentVersion,log,true);
	}

	public SpigetUpdateAbstract setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public SpigetUpdateAbstract setVersionComparator(VersionComparator comparator) {
		this.versionComparator = comparator;
		return this;
	}

	public ResourceInfo getLatestResourceInfo() {
		return latestResourceInfo;
	}

	protected abstract void dispatch(Runnable runnable);

	public boolean isVersionNewer(String oldVersion, String newVersion) {
		return versionComparator.isNewer(oldVersion, newVersion);
	}

	public void checkForUpdate(final UpdateCallback callback) {
		dispatch(new Runnable() {
			@Override
			public void run() {
				try {
					HttpURLConnection connection=null;
					Gson gson=new Gson();
					JsonObject jsonObject = establishConnection(connection,new URL(String.format(RESOURCE_INFO, resourceId, System.currentTimeMillis())));

					latestResourceInfo = new Gson().fromJson(jsonObject, ResourceInfo.class);

					jsonObject = establishConnection(connection,new URL(String.format(RESOURCE_VERSION, resourceId, System.currentTimeMillis())));
					latestResourceInfo.latestVersion = gson.fromJson(jsonObject, ResourceVersion.class);

					//to get update info
					jsonObject=establishConnection(connection,new URL(String.format(UPDATE_INFO,resourceId,System.currentTimeMillis())));
					latestResourceInfo.latestVersion.updateInfo=gson.fromJson(jsonObject,UpdateVersion.class);
					if (isVersionNewer(currentVersion, latestResourceInfo.latestVersion.name)) {
						callback.updateAvailable(latestResourceInfo.latestVersion.name, "https://spigotmc.org/" + latestResourceInfo.file.url, !latestResourceInfo.external,SpigetUpdateAbstract.this);
					} else {
						callback.upToDate(SpigetUpdateAbstract.this);
					}
				} catch (Exception e) {
					callback.failedCheck(e,SpigetUpdateAbstract.this);
					if(canLog)
					log.log(Level.WARNING, "Failed to get resource info from spiget.org", e);
				}
			}
		});
	}
	private JsonObject establishConnection(@Nullable HttpURLConnection connection, URL url) throws IOException {
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", getUserAgent());
		return new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();


	}

}
