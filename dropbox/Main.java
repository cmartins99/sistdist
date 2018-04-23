/*
 Exemplo em https://github.com/dropbox/dropbox-sdk-java
 */
package br.aulas.dropbox.exemplos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

public class Main {

    private static final String ACCESS_TOKEN = "vvkG3EdLzSMAAAAAAAA63-v_2_NFAlwWgmBsNiRK0iJPgVzUbW_rY7nnBaF8xk8R";
    private DbxRequestConfig config = null;
    DbxClientV2 client = null;
    FullAccount account = null;

    Main() {  // metodo construtor
        // Cria o objeto Dropbox cliente
        config = new DbxRequestConfig("dropbox/java-tutorial");
        client = new DbxClientV2(config, ACCESS_TOKEN);

        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println("CONTA DE: " + account.getName().getDisplayName());
        } catch (DbxException dbxe) {
            dbxe.printStackTrace();
        }
    }

    public void createFolder(String folderName) throws DbxException {
        try {
            FolderMetadata folder = client.files().createFolder(folderName);
            System.out.println(folder.getName());
        } catch (CreateFolderErrorException err) {
            if (err.errorValue.isPath() 
                    && err.errorValue.getPathValue().isConflict()) {
                System.out.println("Pasta ja existente.");
            } else {
                System.err.print("Um erro ocorreu na criacao da pasta...");
                System.err.print(err.toString());
            }
        } catch (Exception err) {
            System.err.print("Uma Exception presente...");
            System.err.print(err.toString());
        }
    }

    public void listFolder() {
        try {
            // Get files and folder metadata from Dropbox root directory
            ListFolderResult result = client.files().listFolder("");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    System.out.println(metadata.getPathLower());
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }
        } catch (DbxException dbxe) {
            dbxe.printStackTrace();
        }
    }

    public void uploadFile(String path, String foldername) {
        // Upload (carrega) o arquivo em "path" para o Dropbox
        try {
            InputStream in = new FileInputStream(path);
            FileMetadata metadata = client.files().uploadBuilder(foldername).uploadAndFinish(in);
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (DbxException dbxe) {
            dbxe.printStackTrace();
        }
    }

    public void readDownloadFile(String foldernameFileSource, String filenameTarget) {
        // Arquivo para download armazenado no sistema local em "filenameTarget"
        try {
            
            FileOutputStream downloadFile = new FileOutputStream(filenameTarget);
            try {
                FileMetadata metadata = client.files()
                        .downloadBuilder(foldernameFileSource)
                        .download(downloadFile);
            } finally {
                downloadFile.close();
                System.out.println("Arquivo "+foldernameFileSource+" baixado com sucesso");
            }
        } //exception handled 
        catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String path) {
        try {
            Metadata metadata = client.files().delete(path);
            System.out.println("Arquivo "+path+" apagado com sucesso.");
        } catch (DbxException dbxe) {
            dbxe.printStackTrace();
        }
    }

    // metodo MAIN para testar os metodos.
    public static void main(String args[]) throws DbxException {
        Main m = new Main();
        String folderName = "/test_java_createFolder" + System.currentTimeMillis();
        m.listFolder();
        m.createFolder(folderName);
        m.uploadFile("C:/tmp/livros.txt", folderName + "/livros-lista.txt");
        m.readDownloadFile(folderName + "/livros-lista.txt", "c:/tmp/livros-lista.txt");
        m.deleteFile(folderName + "/livros-lista.txt");
    }
} // fim da classe
