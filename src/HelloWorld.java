import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

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
		long window = GLFW.glfwCreateWindow(800 /* width */, 600 /* height */, "HelloGL", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(window);

		///////////////////////////////////////////////////////////////////////////
		// Set up OpenGL

		GL.createCapabilities();
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glClearDepth(1.0f);

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
                // Coordinates       // Colours
                 0.5f,  0.5f, 0.0f,  1.0f, 0.0f, 0.0f, // Top Right
                 0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f, // Bottom Right
                -0.5f, -0.5f, 0.0f,  0.0f, 0.0f, 1.0f, // Bottom Left
                -0.5f,  0.5f, 0.0f,  0.0f, 0.0f, 0.0f  // Top Left
        };

        int[] indices = new int[] {
                0, 1, 3,
                1, 2, 3
        };
		FloatBuffer fbo = BufferUtils.createFloatBuffer(coords.length);
		fbo.put(coords);                                // Copy the vertex coords into the floatbuffer
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
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * 4, 0);  // Link VBO to VAO attrib 0

        // Colour attribute
        GL20.glEnableVertexAttribArray(1);              // Enable the VAO's second attribute (1)
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * 4, 3 * 4);  // Link VBO to VAO attrib 1


        // Create Element Array Buffer
        int ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        ///////////////////////////////////////////////////////////////////////////
        // Transformations

        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
//        new Matrix4f().perspective((float) Math.toRadians(45.0f), 8.0f / 6.0f, 0.01f, 100.0f)
//                .lookAt(2.0f, 1.5f, 1.5f,
//                        0.0f, 0.0f, 0.0f,
//                        0.0f, 1.0f, 0.0f)
//                .get(fb);
        new Matrix4f().get(fb);

        int mat4Location = GL20.glGetUniformLocation(program, "MVP");
        GL20.glUniformMatrix4fv(mat4Location, false, fb);

		///////////////////////////////////////////////////////////////////////////
		// Loop until window is closed

		while (!GLFW.glfwWindowShouldClose(window)) {
			GLFW.glfwPollEvents();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL30.glBindVertexArray(vao);
            GL11.glDrawElements(GL11.GL_TRIANGLES, 2 * 3, GL11.GL_UNSIGNED_INT, ebo);

            GLFW.glfwSwapBuffers(window);
		}

		///////////////////////////////////////////////////////////////////////////
		// Clean up

		GL15.glDeleteBuffers(vbo);
		GL30.glDeleteVertexArrays(vao);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}


	public static String[] readShader(String filename) {
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
}