package VueControleur;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class MapFileReader 
{
    public File mapsFolderPath = new File("Niveaux"); //le dossier qui contient les fichiers .gyro

    public ArrayList<File> getAllAvailableMapsFiles()
    {
        File[] mapsFilesArray = mapsFolderPath.listFiles(); //les fichiers contenus dans le dossier
        ArrayList<File> mapsFiles = new ArrayList<File>();

        for (File mapFile : mapsFilesArray)
            if (mapFile.getName().toLowerCase().endsWith(".gyro"))
                mapsFiles.add(mapFile);

        return mapsFiles;
    }

    public HashMap<String, ArrayList<ArrayList<Integer>>> loadGameMap(String mapFilename)
	{
        JSONParser parser = new JSONParser();
        HashMap<String, ArrayList<ArrayList<Integer>>> properties = new HashMap<String, ArrayList<ArrayList<Integer>>>();

		try 
        {
			Object obj = parser.parse(new FileReader(mapsFolderPath + "/" + mapFilename));
			JSONObject jsonObject = (JSONObject) obj;

            Set<String> iterator = jsonObject.keySet();

            for (String key : iterator){
                ArrayList<ArrayList<Integer>> elAr = new ArrayList<ArrayList<Integer>>();
                JSONArray colonne = (JSONArray) jsonObject.get(key);
                Iterator ic = colonne.iterator();
                while (ic.hasNext()){
                    JSONObject o = (JSONObject) ic.next();
                    Collection<Long> i = o.values();
                    ArrayList<Integer> eVal = new ArrayList<Integer>();
                    for (Long val : i){
                        eVal.add(val.intValue());
                    }
                    elAr.add(eVal);
                }
                properties.put(key, elAr);
            }
            
            return properties;
		} 
        catch (Exception e)
        {
			e.printStackTrace();
            return null;
		}
	}
}

