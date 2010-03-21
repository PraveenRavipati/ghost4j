/*
 * Ghost4J: a Java wrapper for Ghostscript API.
 *
 * Distributable under LGPL license.
 * See terms of license at http://www.gnu.org/licenses/lgpl.html.
 */

package net.sf.ghost4j.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class used to store and retrieve files to / from  a temporary disk storage.
 * @author Gilles Grousset (gi.grousset@gmail.com)
 */
public class DiskStore {

    public static final String ROOT_PATH = System.getProperty("java.io.tmpdir") + File.separator + "ghost4j";

    /**
     * Shared instance.
     */
    private static DiskStore instance;

    /**
     * Map used to store references to temprorary files.
     */
    private Map<String, File> map;

    /**
     * Access to the shared instance.
     * @return The shared DiskStore instance.
     */
    public static synchronized DiskStore getInstance(){

        if (instance == null){
            instance = new DiskStore();
        }
        
        return instance;
        
    }

    /**
     * Private constructor.
     */
    private DiskStore(){

        map = new HashMap<String, File>();

        //register a shutdown hook to ensure temporary files are deleted at shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                super.run();

                DiskStore diskStore = DiskStore.getInstance();

                //remove all files when store is destroyed
                try{
                    for(String key : map.keySet()){
                        diskStore.removeFile(key);
                    }

                    // remove root dir
                    new File(ROOT_PATH).delete();

                } catch(Exception e){
                    //fail silently...
                }
            }
       });

    }

    /**
     * Retrieve a File from a store key.
     * If key iss unknow, null is returned.
     * @param key Unique file resource identifier.
     * @return File or null (if not found).
     */
    public File getFile(String key){

        return map.get(key);

    }

    /**
     * Remove a file from the store.
     * This also deleted the temporary file from the file system.
     * @param key Unique file resource identifier.
     * @throws IOException In case the file cannot be deleted.
     */
    public void removeFile(String key) throws IOException{

        File file = this.getFile(key);

        if (file != null){

            // delete file
            if (!file.delete()){
                throw new IOException("Temporary file " + file.getAbsolutePath() + " cannot be deleted");
            }

            // remove from map
            map.remove(key);
        }
    }

    /** Add a file to the store.
     * @param key File unique identifier.
     * @return The generated (empty) file.
     */
    public File addFile(String key){

        //prepare file
        File file = new File(ROOT_PATH, key);

        //ensure store root is created
        file.getParentFile().mkdirs();

        //add to map
        map.put(key, file);

        return file;

    }


}
