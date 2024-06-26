package daat.helper;
// Copyright 2018 Google LLC

//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class GoogleDriverHelper {
    private static final String APPLICATION_NAME = "Google Drive API Java";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static HttpTransport HTTP_TRANSPORT;
    private static String fileId = null;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private String credentialPath;
    private String storePath;
    private Drive service;

    public GoogleDriverHelper(String credential) {
        this.credentialPath = credential;
        storePath = configStorePath(credentialPath);
        try {
            service = getDriveService();
        } catch (IOException ex) {
            return;
        }
    }

    private Drive getDriveService() throws IOException {
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * At first time, gmail will display consent window to ask for authorize then
     * store creadential binary file
     * to folder ${Project_directory}/credentials/{Credential_json_fileName}
     * Please authorize and commit to automation source for skip step authorize next
     * run
     *
     * @param credentialJson
     * @return
     */
    private String configStorePath(String credentialJson) {
        credentialJson = credentialJson.replace(".json", "");
        String folderName = credentialJson.substring(credentialJson.lastIndexOf("\\") + 1);
        return System.getProperty("user.dir") + java.io.File.separator + "credentials" + java.io.File.separator
                + folderName;
    }

    private Credential authorize() throws IOException {
        // Load client secrets.
        String api_key = getCredentialPath();
        InputStream in = new FileInputStream(api_key);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        java.io.File data_store_dir = new java.io.File(getStorePath());
        FileDataStoreFactory data_store_factory = new FileDataStoreFactory(data_store_dir);
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                        .setDataStoreFactory(data_store_factory)
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private String getCredentialPath() {
        return credentialPath;
    }

    private String getStorePath() {
        return storePath;
    }

    public void upload(String fileName, String filePath, String fileType) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        java.io.File path = new java.io.File(filePath);
        FileContent mediaContent = new FileContent(fileType, path);
        service.files().create(fileMetadata, mediaContent)
                .setFields("name")
                .execute();
    }

    public void uploadOverride(String fileName, String filePath, String fileType) throws IOException {
        for (File f : getListFile()) {
            if (f.getName().equals(fileName)) {
                delete(f.getId());
            }
        }
        upload(fileName, filePath, fileType);
    }

    public List<File> getListFile() throws IOException {
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        return result.getFiles();
    }

    private String getIdFromFileName(String fileName) throws IOException {
        List<File> list = getListFile();
        for(File f: list){
            if (f.getName().equals(fileName)) return f.getId();
        }
        return null;
    }

    public void delete(String id) throws IOException {
        service.files().delete(id).execute();
    }

    public void updateFile(String fileName, String filePath, String fileType) throws IOException {
        if (getIdFromFileName(fileName) == null) {
            upload(fileName, filePath, fileType);
        }
        if (fileId == null) {
            fileId = getIdFromFileName(fileName);
        }
        java.io.File path = new java.io.File(filePath);
        File file = new File();
        file.setName(fileName);
        FileContent mediaContent = new FileContent(fileType, path);
        service.files().update(fileId, file, mediaContent).execute();
    }
}
