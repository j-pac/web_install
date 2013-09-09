package user;

public class User {

	private String nick_name;
	private Picture main_picture;
	
	public void setNickname(String nick) {
		nick_name = nick;
	}
	
	public void attributePicture(Picture pic) {
		main_picture = pic;
	}
	
	public String getNickname() {
		return nick_name;
	}
	
	public Picture getPicture() {
		return main_picture;
	}
	
}
