package recommend;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import com.wrapper.spotify.requests.AbstractRequest;

import com.wrapper.spotify.model_objects.specification.Category;
import com.wrapper.spotify.requests.data.browse.GetListOfCategoriesRequest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import java.util.*; //for ArrayList
import java.lang.*;

/**
 * Uses spotify API to send back recommended songs to client based on user's songs from client.
 *
 */

public class RecSongs
{
    
    //instance variables 
    private final String clientId = "a1836f665e904cb4869901a56eb1582b";
    private final String clientSecret = "cfb9be2440aa427ca22fa07d583833d7";
    private final int songsToReturn = 3;
    private int playlistLength;

    //authentification
    private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
          .setClientId(clientId)
          .setClientSecret(clientSecret)
          .build();
    private final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
          .build();

    public void clientCredentials_Sync() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

        // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        }
        catch (IOException | SpotifyWebApiException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    //Constructor - intialize clientcredentials
    RecSongs()
    {
        clientCredentials_Sync();
        playlistLength = 0;
        System.out.println("client credentials authorized.");
    }

    
    //returns arraylist of jsonobjects songs given a categoryId (genre)
    public ArrayList<JSONObject> getSongs(String categoryId)
    {
        
        System.out.println("retrieiving songs list");
        ArrayList<JSONObject> songList = new ArrayList<JSONObject>();
        String playlistId = getCategoryPlaylist(categoryId);
        Paging<PlaylistTrack> playlist = getPlaylistTracks(playlistId);
        int i=0;
        while (playlist.getItems().length<3)
        {
            playlistId = getCategoryPlaylist(categoryId);
            playlist = getPlaylistTracks(playlistId);
        }
        System.out.println("about to entire while loop");
        System.out.println("playlist length: " + playlist.getItems().length);
        while (i<songsToReturn)
        {
            System.out.println("inside while loop " + i + " iteration");
            JSONObject obj = new JSONObject();
            obj.put("song", playlist.getItems()[i].getTrack().getName());
            obj.put("artist", playlist.getItems()[i].getTrack().getArtists()[0].getName());
            obj.put("genre", categoryId);
            songList.add(obj);
            i++;
        }
        System.out.println("exited while loop");
        System.out.print(songList);
        System.out.println("got songs list");
        return songList;
        
        
    }

    //How we will do this: Get catgegory playlist --> Get playlist's tracks

    //Get list of categories
    public void getCategories()
    {
        GetListOfCategoriesRequest getListOfCategoriesRequest = spotifyApi.getListOfCategories()
          .country(CountryCode.US)
          .limit(50)
          .offset(0)
          .locale("sv_SE")
          .build();
        try
        {
            Paging<Category> categoryPaging = getListOfCategoriesRequest.execute();
            System.out.println(categoryPaging.getTotal());
            int length = categoryPaging.getItems().length;
            for (int i=0; i<length; i++)
            {
                System.out.println("name: "+categoryPaging.getItems()[i].getName());
                System.out.println("id: "+categoryPaging.getItems()[i].getId());
            }
        }
        catch (IOException | SpotifyWebApiException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Get category playlist
    private String getCategoryPlaylist(String categoryId)
    {
        System.out.println("getting category playlist");
        GetCategorysPlaylistsRequest getCategoryRequest = spotifyApi.getCategorysPlaylists(categoryId)
            .country(CountryCode.US)
            .limit(1)
            .offset((int) Math.floor(Math.random() * 6))
            .build();
        try 
        {
            //System.out.println(getCategoryRequest.getJson());
            Paging<PlaylistSimplified> playlistSimplifiedPaging = getCategoryRequest.execute();
            System.out.println("PlaylistID: " + playlistSimplifiedPaging.getItems()[0].getId());
            System.out.println("PlaylistName: " + playlistSimplifiedPaging.getItems()[0].getName());
            System.out.println("Playlist Length: " + playlistSimplifiedPaging.getItems()[0].getTracks().getTotal());
            playlistLength = playlistSimplifiedPaging.getItems()[0].getTracks().getTotal();
            return playlistSimplifiedPaging.getItems()[0].getId();
        }
        catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //Get playlist's tracks
    private Paging<PlaylistTrack> getPlaylistTracks(String playlistId)
    {
        System.out.println("getting playlist tracks");
        GetPlaylistsTracksRequest getPlaylistsTracksRequest = spotifyApi
          .getPlaylistsTracks(playlistId)
          .limit(3)
          .offset((int) Math.floor(Math.random() * (playlistLength-4)))
          .market(CountryCode.US)
          .build();
        try
        {
            //System.out.println(getPlaylistsTracksRequest.getJson());
            Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();  
            System.out.println("got playlist tracks");
            return playlistTrackPaging;
        }
        catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
}

