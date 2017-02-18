import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class HelloWorld {

	public static void main(String[] args) {

		///////////////////////////////////////////////////////////////////////////
		// Set up GLFW window

		GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
		GLFW.glfwSetErrorCallback(errorCallback);
		GLFW.glfwInit();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		long window = GLFW.glfwCreateWindow(800 /* width */, 800 /* height */, "HelloGL", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(window);

		///////////////////////////////////////////////////////////////////////////
		// Set up OpenGL

		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.2f);
		GL11.glClearDepth(1.0f);

        // Enable depth test
        glEnable(GL_DEPTH_TEST);
        // Accept fragment if it closer to the camera than the former one
        glDepthFunc(GL_LESS);

		///////////////////////////////////////////////////////////////////////////
		// Set up minimal shader programs

		// Compile vertex shader
		int vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vs, (CharSequence[]) readShader("vertex.vert"));
		GL20.glCompileShader(vs);

		// Compile fragment shader
		int fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fs, (CharSequence[]) readShader("fragment.frag"));
		GL20.glCompileShader(fs);

		// Link vertex and fragment shaders into an active program
		int program = GL20.glCreateProgram();
		GL20.glAttachShader(program, vs);
		GL20.glAttachShader(program, fs);
		GL20.glLinkProgram(program);
		GL20.glUseProgram(program);

		///////////////////////////////////////////////////////////////////////////
		// Set up data

		// Fill a Java FloatBuffer object with memory-friendly floats
		float[] coords = new float[] {
                // Coordinates       // Colours         // Textures
                 0.5f,  0.5f,  0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, // Top Right
                 0.5f, -0.5f,  0.0f,  0.0f, 1.0f, 1.0f,  1.0f, 0.0f, // Bottom Right
                -0.5f, -0.5f,  0.0f,  1.0f, 0.0f, 1.0f,  0.0f, 0.0f, // Bottom Left
                -0.5f,  0.5f,  0.0f,  0.0f, 0.5f, 0.0f,  0.0f, 1.0f, // Top Left
                 0.5f,  0.5f, -1.0f,  1.0f, 0.0f, 0.0f,  1.0f, 1.0f, // Top Right
                 0.5f, -0.5f, -1.0f,  0.0f, 0.0f, 1.0f,  1.0f, 0.0f, // Bottom Right
                -0.5f, -0.5f, -1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 0.0f, // Bottom Left
                -0.5f,  0.5f, -1.0f,  0.0f, 1.0f, 0.5f,  0.0f, 1.0f  // Top Left
        };

        float vertices[] = {
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f,  0.0f, 0.0f, -1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 1.0f,  0.0f, 0.0f, -1.0f,
                0.5f, 0.5f, -0.5f,   1.0f, 1.0f, 0.0f,  0.0f, 0.0f, -1.0f,
                0.5f, 0.5f, -0.5f,   1.0f, 1.0f, 0.0f,  0.0f, 0.0f, -1.0f,
                -0.5f, 0.5f, -0.5f,  1.0f, 0.0f, 1.0f,  0.0f, 0.0f, -1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f,  0.0f, 0.0f, -1.0f,

                -0.5f, -0.5f, 0.5f,  0.0f, 1.0f, 1.0f,   0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f,   1.0f, 0.0f, 1.0f,   0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.5f,    1.0f, 1.0f, 0.0f,   0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.5f,    1.0f, 1.0f, 0.0f,   0.0f, 0.0f, 1.0f,
                -0.5f, 0.5f, 0.5f,   1.0f, 0.0f, 1.0f,   0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f,  0.0f, 1.0f, 1.0f,   0.0f, 0.0f, 1.0f,

                -0.5f, 0.5f, 0.5f,   0.0f, 1.0f, 1.0f,  -1.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f,  1.0f, 0.0f, 1.0f,  -1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f,  -1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f,  -1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, 0.5f,  1.0f, 0.0f, 1.0f,  -1.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f,   0.0f, 1.0f, 1.0f,  -1.0f, 0.0f, 0.0f,

                0.5f, 0.5f, 0.5f,    1.0f, 1.0f, 0.0f,  1.0f, 0.0f, 0.0f,
                0.5f, 0.5f, -0.5f,   1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 1.0f,  1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 1.0f,  1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f,   1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f,    1.0f, 1.0f, 0.0f,  1.0f, 0.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f,  0.0f, -1.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 1.0f,  0.0f, -1.0f, 0.0f,
                0.5f, -0.5f, 0.5f,   1.0f, 1.0f, 0.0f,  0.0f, -1.0f, 0.0f,
                0.5f, -0.5f, 0.5f,   1.0f, 1.0f, 0.0f,  0.0f, -1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f,  1.0f, 0.0f, 1.0f,  0.0f, -1.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f,  0.0f, -1.0f, 0.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 1.0f,   0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f,  1.0f, 0.0f, 1.0f,   0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f,  1.0f, 0.0f, 1.0f,   0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 1.0f,   0.0f, 1.0f, 0.0f
        };


        ///////////////////////////////////////////////////////////////////////////
        // Textures

        int squareSize = 2;
        int squareNum = 8;
        int texSize = squareSize * squareNum;
        byte[] board = genCheckerboard(squareSize, squareNum, null, null);
        ByteBuffer wrappedRGBA = ByteBuffer.wrap(board);

        int checkerboardTexID = GL11.glGenTextures();
        GL13.glActiveTexture(checkerboardTexID);
        GL11.glBindTexture(GL_TEXTURE_2D, checkerboardTexID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Texture wrapping parameters
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // Texture filtering parameters
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        // Load texture
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, texSize,
                texSize, 0, GL_RGBA, GL_UNSIGNED_BYTE, wrappedRGBA);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        GL11.glBindTexture(GL_TEXTURE_2D, 0);


        ///////////////////////////////////////////////////////////////////////////
        // Buffers

        FloatBuffer fbo = BufferUtils.createFloatBuffer(vertices.length);
		fbo.put(vertices);                              // Copy the vertex coords into the floatbuffer
		fbo.flip();                                     // Mark the floatbuffer ready for reads

		// Store the FloatBuffer's contents in a Vertex Buffer Object
		int vbo = GL15.glGenBuffers();                  // Get an OGL name for the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);   // Activate the VBO
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fbo, GL15.GL_STATIC_DRAW);  // Send VBO data to GPU

        // Bind the VBO in a Vertex Array Object
		int vao = GL30.glGenVertexArrays();             // Get an OGL name for the VAO
		GL30.glBindVertexArray(vao);                    // Activate the VAO

        // Position attribute
		GL20.glEnableVertexAttribArray(0);              // Enable the VAO's first attribute (0)
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 9 * 4, 0);  // Link VBO to VAO attrib 0

        // Colour attribute
        GL20.glEnableVertexAttribArray(1);              // Enable the VAO's second attribute (1)
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 9 * 4, 3 * 4);  // Link VBO to VAO attrib 1

        // Texture attribute
        GL20.glEnableVertexAttribArray(2);              // Enable the VAO's third attribute (2)
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 9 * 4, 6 * 4);  // Link VBO to VAO attrib 2

        // Create Element Array Buffer
//        int ebo = GL15.glGenBuffers();
//        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
//        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        GL30.glBindVertexArray(0);


        ///////////////////////////////////////////////////////////////////////////
        // Transformations

        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        Matrix4f ident = new Matrix4f();
        Matrix4f ortho = new Matrix4f()
                .ortho(-1.2f, 1.2f, -1.0f, 1.0f, 0.01f, 100.0f)
                .lookAt(1.5f, 1.5f, 2.5f,
                        0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f);

        Matrix4f persp = new Matrix4f()
                .perspective((float) Math.toRadians(45.0f), 1, 0.01f, 100.0f)
                .lookAt(1.5f, 1.5f, 2.5f,
                        0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f);

        persp.get(fb);

        int mat4Location = GL20.glGetUniformLocation(program, "MVP");
        GL20.glUniformMatrix4fv(mat4Location, false, fb);

		///////////////////////////////////////////////////////////////////////////
		// Loop until window is closed

		while (!GLFW.glfwWindowShouldClose(window)) {
			GLFW.glfwPollEvents();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);

            GL13.glActiveTexture(checkerboardTexID);
            GL11.glBindTexture(GL_TEXTURE_2D, checkerboardTexID);

			GL30.glBindVertexArray(vao);
//            GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length * 3, GL11.GL_UNSIGNED_INT, ebo);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / 3);
            GL30.glBindVertexArray(0);



            GLFW.glfwSwapBuffers(window);
		}

		///////////////////////////////////////////////////////////////////////////
		// Clean up

		GL15.glDeleteBuffers(vbo);
        GL11.glDeleteTextures(checkerboardTexID);
		GL30.glDeleteVertexArrays(vao);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}


	private static String[] readShader(String filename) {
		List<String> lines = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader("/Users/Dima/Documents/Programming/Java/Graphics/IntelliJ/shaders/" + filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();

		} catch	(IOException i) {
			System.err.println(i.getMessage());
		}
        String[] arr = lines.toArray(new String[lines.size()]);
        arr[0] += '\n';
		return arr;
	}

    private static byte[] genCheckerboard(int squareSize, int numSquares, Color color1, Color color2) {
        int size = squareSize * numSquares;
        byte[] checkImage = new byte[size * size * 4]; //holds the RGBA texel values
        Color color;
        int index = 0;
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                // Determine the "super-column" and "super-row"
                int superCol = col / squareSize; //integer division truncates to
                int superRow = row / squareSize; // whole super column/row
                //add the column and row together. If the result is even then the
                // texel falls in a location which is color1; if odd, color2
                boolean odd = ((superCol + superRow) % 2) == 0;
                if (((superCol + superRow) % 2) == 0) {
                    color = color1;
                } else {
                    color = color2;
                }
//                checkImage[index++] = (byte) color.getRed();
//                checkImage[index++] = (byte) color.getGreen();
//                checkImage[index++] = (byte) color.getBlue();
//                checkImage[index++] = (byte) 255;
                checkImage[index++] = (byte) (odd ? 0 : 255);
                checkImage[index++] = (byte) (odd ? 255 : 0);
                checkImage[index++] = (byte) (odd ? 255 : 0);
                checkImage[index++] = (byte) 255;

            }
        }

        return checkImage;
    }
}