package tiled;

public interface TiledMapReader {

	CustomTiledMap readTiledMap(String fileName) throws TiledMapIoException;
	
}
