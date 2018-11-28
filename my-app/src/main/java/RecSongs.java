

import org.json.simple.JSONObject;

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

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Uses spotify API to send back recommended songs to client based on user's songs from client.
 *
 */

public class RecSongs
{
    
    //instance variables 
    private final String clientId = "a1836f665e904cb4869901a56eb1582b";
    private final String clientSecret = "cfb9be2440aa427ca22fa07d583833d7";

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
    }

    
    //returns 3 songs given a categoryId (genre)
    public JSONObject getSongs(String categoryId)
    {
        String playlistId = getCategoryPlaylist(categoryId);
        Paging<PlaylistTrack> playlist = getPlaylistTracks(playlistId);
        JSONObject obj = new JSONObject();
        int i=0;
        while (playlist.getNext()!=null && i<playlist.getTotal());
        {
            obj.put("song", playlist.getItems()[i].getTrack().getName());
            obj.put("artist", playlist.getItems()[i].getTrack().getArtists());
            obj.put("genre", categoryId);
            playlist.getNext();
            i++;
        }
        System.out.print(obj);
        return obj;
    }

    //How we will do this: Get catgegory playlist --> Get playlist's tracks

    //Get category playlist
    private String getCategoryPlaylist(String categoryId)
    {
        GetCategorysPlaylistsRequest getCategoryRequest = spotifyApi.getCategorysPlaylists(categoryId)
            .country(CountryCode.US)
            .limit(1)
            .offset(0)
            .build();
        try 
        {
            Paging<PlaylistSimplified> playlistSimplifiedPaging = getCategoryRequest.execute();
            System.out.println("PlaylistID: " + playlistSimplifiedPaging.getItems()[0].getId());
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
        GetPlaylistsTracksRequest getPlaylistsTracksRequest = spotifyApi
          .getPlaylistsTracks(playlistId)
          .limit(3)
          .offset(0)
          .market(CountryCode.US)
          .build();
        try
        {
            Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();  
            return playlistTrackPaging;
        }
        catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
}

