```java
1. Start
2. Import the required libraries: org.joml.*, org.lwjgl.BufferUtils, javax.print.DocFlavor, java.io.IOException, java.nio.FloatBuffer, java.nio.file.Files, java.nio.file.Paths
3. Declare a class called Shader
4. Declare private instance variables:
    - shaderProgramID of type int
    - beingUsed of type boolean
    - vertexSource, fragmentSource, and filepath of type String
5. Define a constructor that takes a filepath as a parameter
1. Set the filepath instance variable to the provided filepath
2. Try the following:
1. Read the contents of the file specified by the filepath and store it in a variable named source
2. Split the source string using the pattern "(#type)( )+([a-zA-Z]+)" and store the resulting array in splitString
3. Find the first pattern after "#type 'pattern'"
    - Extract the substring from index to eol (end of line)
    - Trim the extracted substring and store it in the variable firstPattern
4. Find the second pattern after "#type 'pattern'"
    - Extract the substring from index to eol (end of line)
    - Trim the extracted substring and store it in the variable secondPattern
5. If firstPattern is "vertex", set vertexSource to splitString[1]
    - If firstPattern is "fragment", set fragmentSource to splitString[1]
    - Otherwise, throw an IOException with the message "Unexpected token 'firstPattern'"
6. If secondPattern is "vertex", set vertexSource to splitString[2]
    - If secondPattern is "fragment", set fragmentSource to splitString[2]
    - Otherwise, throw an IOException with the message "Unexpected token 'secondPattern'"
3. Catch an IOException and do the following:
    - Print the stack trace
    - Assert false with the message "Error: Could not open file for shader: 'filepath'"
6. Define a method called compile()
    1. Declare local variables: vertexID and fragmentID of type int
    2. Create a vertex shader by calling glCreateShader(GL_VERTEX_SHADER) and store the ID in vertexID
    3. Pass the vertexSource to the GPU by calling glShaderSource(vertexID, vertexSource)
    4. Compile the vertex shader by calling glCompileShader(vertexID)
    5. Check for compilation errors by calling glGetShaderi(vertexID, GL_COMPILE_STATUS)
        - If the compilation fails, retrieve the info log and print an error message
        - Assert false
    6. Create a fragment shader by calling glCreateShader(GL_FRAGMENT_SHADER) and store the ID in fragmentID
    7. Pass the fragmentSource to the GPU by calling glShaderSource(fragmentID, fragmentSource)
    8. Compile the fragment shader by calling glCompileShader(fragmentID)
    9. Check for compilation errors by calling glGetShaderi(fragmentID, GL_COMPILE_STATUS)
        - If the compilation fails, retrieve the info log and print an error message
        - Assert false
    10. Create a shader program by calling glCreateProgram() and store the ID in shaderProgramID
    11. Attach the vertex shader and fragment shader to the shader program by calling glAttachShader(shaderProgramID, vertexID) and glAttachShader(shaderProgramID, fragmentID)
    12. Link the shader program by calling glLinkProgram(shaderProgramID)
    13. Check for linking errors by calling glGetProgrami(shaderProgramID, GLLINK_STATUS)
        - If the linking fails, retrieve the info log and print an error message
        - Assert false
7. Define a method called use()
    1. If beingUsed is false, do the following:
        - Bind the shader program by calling glUseProgram(shaderProgramID)
        - Set beingUsed to true
8. Define a method called detach()
    1. Call glUseProgram(0) to unbind the shader program
    2. Set beingUsed to false
9. Define a method called uploadMat4f() that takes varName as a string and mat4 as a Matrix4f
    1. Get the uniform location for varName by calling glGetUniformLocation(shaderProgramID, varName) and store it in varLocation
    2. Call use() to ensure the shader program is active
    3. Create a FloatBuffer named matBuffer using BufferUtils.createFloatBuffer(16)
    4. Call mat4.get(matBuffer) to store the matrix values in the buffer
    5. Call glUniformMatrix4fv(varLocation, false, matBuffer) to upload the matrix to the GPU
10. Define a method called uploadIntArray() that takes varName as a string and array as an int array
    1. Get the uniform location for varName by calling glGetUniformLocation(shaderProgramID, varName) and store it in varLocation
    2. Call use() to ensure the shader program is active
    3. Call glUniform1iv(varLocation, array) to upload the int array to the GPU
11. End

```