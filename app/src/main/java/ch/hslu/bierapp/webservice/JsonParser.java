package ch.hslu.bierapp.webservice;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.bierapp.common.Beer;

public class JsonParser {

    private static final String TAG_BREWERIES = "breweries";
    private static final String TAG_URL = "url";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_BEERS = "beers";
    private static final String TAG_NUM_RATINGS = "num_ratings";
    private static final String TAG_SEASONAL = "seasonal";
    private static final String TAG_BREWED_AT = "brewed_at";
    private static final String TAG_STYLE = "style";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_BREWERY_URL = "brewery_url";
    private static final String TAG_CALORIES = "calories";
    private static final String TAG_BREWERY = "brewery";
    private static final String TAG_ABV = "abv";
    private static final String TAG_BREWED_AT_URL = "brewed_at_url";
    private static final String TAG_CITY = "city";
    private static final String TAG_COUNTRY = "country";
    private static final String TAG_IMG = "img";

    /**
     * Extracts all beer entries out of a json string.
     * {'breweries': [], 'beers': [{'url': '/beer/doppelleu-chopfab-amber/209053/',
     * 'num_ratings': 19, 'id': 209053, 'name': 'Doppelleu Chopfab Amber', 'overall_rating': 51}]}
     * @param json json string to parse
     * @return List of {@link ch.hslu.bierapp.common.Beer}s
     */
    public static List<Beer> getAllBeersFromJson(String json) {
        List<Beer> beerList = new ArrayList<>();

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            JSONArray jsonBeers = jsonObj.getJSONArray(TAG_BEERS);

            for (int i = 0; i < jsonBeers.length(); i++) {
                Beer beer = new Beer();
                JSONObject jsonBeer = jsonBeers.getJSONObject(i);

                beer.setTitle(jsonBeer.getString(TAG_NAME));
                beer.setBeerRestId(jsonBeer.getString(TAG_ID));
                beer.setBeerRestUrl(jsonBeer.getString(TAG_URL));

                beerList.add(beer);
            }
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "Error parsing json to get all beers" + e.toString());
        }

        return beerList;
    }

    /**
     * Extracts a single beer out of a json string.
     * {'style': 'Amber Ale', 'description': 'KRISTALLKLAR,EDLER HOPFEN',
     * 'brewery_url': '/brewers/doppelleu-brauwerkstatt/16497/',
     * 'url': '/beer/doppelleu-chopfab-amber/209053/', 'mean_rating': '3.26/5.0',
     * 'calories': 162.0, 'num_ratings': 19, 'abv': 5.4, 'style_rating': 77,
     * 'overall_rating': 51, 'brewery': 'Doppelleu Brauwerkstatt', 'weighted_avg': 3.13,
     * 'name': 'Doppelleu Chopfab Amber'}
     * @param json json string to parse
     * @return Instance of {@link ch.hslu.bierapp.common.Beer}
     */
    public static Beer getBeerFromJson(String json) {
        Beer beer = new Beer();
        JSONObject jsonBeer = null;
        try {
            jsonBeer = new JSONObject(json);
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "Error parsing json get one beer " + e.toString());
        }

        try {
            beer.setBeerRestUrl(jsonBeer.getString(TAG_URL));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No Beer URL available.");
        }
        try {
            beer.setTitle(jsonBeer.getString(TAG_NAME));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No Name available.");
        }
        try {
            beer.setAlcoholContent(jsonBeer.getDouble(TAG_ABV));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No ABV available.");
        }
        try {
            beer.setBrewery(jsonBeer.getString(TAG_BREWERY));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No Brewery available.");
        }
        try {
            beer.setBreweryRestUrl(jsonBeer.getString(TAG_BREWERY_URL));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No Brewery URL available.");
        }
        try {
            beer.setCalories(jsonBeer.getInt(TAG_CALORIES));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No Calories available.");
        }
        try {
            beer.setText(jsonBeer.getString(TAG_DESCRIPTION));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No Brewery Descritpion available.");
        }
        try {
            beer.setImageLink(jsonBeer.getString(TAG_IMG));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "No Image available.");
        }

        return beer;
    }

    /**
     * Checks if a given json string contains a single beer.
     * @param json json string to check
     * @return true if json contains single beer, false otherwise
     */
    public static boolean isSingleBeer(String json) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "Error parsing json object to check if single beer" + e.toString());
        }

        return !jsonObj.has(TAG_BEERS);
    }

    /**
     * Checks if a given json string contains brewery informations.
     * @param json json string to check
     * @return true if json contains brewery, false otherwise
     */
    public static boolean isBrewery(String json) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "Error parsing json object to check if brewery" + e.toString());
        }

        return jsonObj.has(TAG_CITY);
    }

    /**
     * Extracts the origin (city, country) out of a json string.
     * @param json json string to parse
     * @return origin string in format "city, country"
     */
    public static String getOrigin(String json) {
        JSONObject jsonOrigin = null;
        String returnString = "";
        try {
            jsonOrigin = new JSONObject(json);

            returnString = (jsonOrigin.getString(TAG_CITY) + ", " + jsonOrigin.getString(TAG_COUNTRY));
        } catch (JSONException e) {
            Log.w("BierApp:JsonParser", "Error parsing json object to get origin" + e.toString());
        }

        return returnString;
    }
}
