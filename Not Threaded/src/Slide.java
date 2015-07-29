public class Slide {

	// Default origin based on GUI initial values
	public static int DEFAULT_X = 225;
	public static int DEFAULT_Y = 500;

	public String caption;
	public String imagePath;
	public int x;
	public int y;

	public Slide() {
		this.caption = "";
		this.imagePath = null;
		this.x = DEFAULT_X;
		this.y = DEFAULT_Y;
	}

	public Slide(String caption, String imagePath) {
		this.caption = caption;
		this.imagePath = imagePath;
		this.x = DEFAULT_X;
		this.y = DEFAULT_Y;
	}

	public Slide(String caption, String filePath, int x, int y) {
		this.caption = caption;
		this.imagePath = filePath;
		this.x = x;
		this.y = y;
	}

	public Slide(Slide slide) {
		this.caption = slide.caption;
		this.imagePath = slide.imagePath;
		this.x = slide.x;
		this.y = slide.y;
	}
}
