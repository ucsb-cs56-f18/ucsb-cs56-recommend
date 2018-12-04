package recommend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //This tells Hibernate to make a table out of this class
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private String song;

	private String artist;

	private String genre;

	public Integer getId() {
		return id;
	}

	public void setID(Integer id) {
		this.id = id;
	}

	public String getSongName() {
		return song;
	}

	public void setSongName(String song) {
		this.song = song;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
}