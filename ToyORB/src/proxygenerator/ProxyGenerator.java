package proxygenerator;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

public class ProxyGenerator {
    public static void generateClientProxySource(Class<?> interfaceClass) {
        StringBuilder sourceCode = new StringBuilder();
        String interfaceName = interfaceClass.getSimpleName();

        sourceCode.append("package clientproxies;\n\n");
        sourceCode.append("import commons.Address;\n");
        sourceCode.append("import messagemarshaller.Marshaller;\n");
        sourceCode.append("import messagemarshaller.Message;\n");
        sourceCode.append("import requestreply.Requestor;\n\n");
        sourceCode.append("import " + interfaceName.toLowerCase() + "app." + interfaceName + ";\n");
        sourceCode.append("public class ").append(interfaceName).append("ClientSideProxy implements ").append(interfaceName).append(" {\n");
        sourceCode.append("\tprivate Address serverAddress;\n\n");
        sourceCode.append("\tpublic ").append(interfaceName).append("ClientSideProxy(Address serverAddress) {\n");
        sourceCode.append("\t\tthis.serverAddress = serverAddress;\n");
        sourceCode.append("\t}\n\n");

        sourceCode.append("\tpublic String sendRequest(String request) {\n");
        sourceCode.append("\t\tRequestor r = new Requestor(\"").append(interfaceName).append("Client\");\n");
        sourceCode.append("\t\tMarshaller m = new Marshaller();\n\n");
        sourceCode.append("\t\tMessage msg = new Message(\"").append(interfaceName).append("Client\", request);\n");
        sourceCode.append("\t\tbyte[] bytes = m.marshal(msg);\n");
        sourceCode.append("\t\tbytes = r.deliver_and_wait_feedback(serverAddress, bytes);\n");
        sourceCode.append("\t\tMessage answer = m.unmarshal(bytes);\n\n");
        sourceCode.append("\t\treturn answer.data;\n\t}");

        for (Method method : interfaceClass.getMethods()) {
            sourceCode.append("\t@Override\n");
            sourceCode.append("\tpublic ").append(method.getReturnType().getSimpleName()).append(" ").append(method.getName()).append("(");

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                sourceCode.append(parameterTypes[i].getSimpleName()).append(" param").append(i);
                if (i < parameterTypes.length - 1) {
                    sourceCode.append(", ");
                }
            }
            sourceCode.append(") {\n");

            sourceCode.append("\t\tString message = \"").append(method.getName()).append(":\"");
            for (int i = 0; i < parameterTypes.length; i++) {
                sourceCode.append(" + param").append(i);
                if (i < parameterTypes.length - 1) {
                    sourceCode.append(" + \":\"");
                }
            }
            sourceCode.append(";\n");

            sourceCode.append("\t\tString answer = sendRequest(message);\n");
            sourceCode.append("\t\treturn answer;\n");
            sourceCode.append("\t}\n\n");
        }

        sourceCode.append("}\n");

        String fileName = interfaceName + "ClientSideProxy.java";
        writeToFile(sourceCode.toString(), fileName, "clientproxies");

        if(compile("clientproxies", fileName)) {
            System.out.println("Compilation complete.");
        } else {
            System.out.println("Compilation failed.");
        }
    }

    public static void generateServerProxySource(Class<?> interfaceClass, int port) {
        StringBuilder sourceCode = new StringBuilder();
        String interfaceName = interfaceClass.getSimpleName();

        sourceCode.append("package serverproxies;\n\n");
        sourceCode.append("import commons.Address;\n");
        sourceCode.append("import " + interfaceName.toLowerCase() + "app." + interfaceName + ";\n");
        sourceCode.append("import " + interfaceName.toLowerCase() + "app." + interfaceName + "Impl;\n");
        sourceCode.append("import messagemarshaller.Message;\n");
        sourceCode.append("import registry.Entry;\n");
        sourceCode.append("import requestreply.ByteStreamTransformer;\n");
        sourceCode.append("import requestreply.Replyer;\n\n");
        sourceCode.append("import java.lang.reflect.Method;\n");
        sourceCode.append("import java.lang.reflect.Parameter;\n\n");

        sourceCode.append("class ").append(interfaceName).append("Server implements Server {\n");
        sourceCode.append("\tpublic ").append(interfaceName).append(" serverObject;\n\n");
        sourceCode.append("\tpublic ").append(interfaceName).append("Server() {\n");
        sourceCode.append("\t\tthis.serverObject = new ").append(interfaceName).append("Impl();\n\t}\n\n");

        sourceCode.append("\tpublic Message get_answer(Message msg) {\n");
        sourceCode.append("\t\tSystem.out.println(\"Server received \" + msg.data + \" from \" + msg.sender);\n\n");
        sourceCode.append("\t\tString[] parts = msg.data.split(\":\");\n");
        sourceCode.append("\t\tString operation = parts[0];\n\n");

        sourceCode.append("\t\ttry {\n");
        sourceCode.append("\t\t\tString methodName = operation;\n");
        sourceCode.append("\t\t\tObject[] parameters = new Object[parts.length - 1];\n\n");
        sourceCode.append("\t\t\tfor(int i = 1; i < parts.length; i++) {\n");
        sourceCode.append("\t\t\t\tparameters[i - 1] = parts[i];\n");
        sourceCode.append("\t\t\t}\n");

        sourceCode.append("\t\t\tMethod[] methods = serverObject.getClass().getMethods();\n");
        sourceCode.append("\t\t\tMethod method = null;\n");
        sourceCode.append("\t\t\tfor(Method m : methods) {\n");
        sourceCode.append("\t\t\t\tif(m.getName().equals(methodName) && m.getParameterCount() == parameters.length) {\n");
        sourceCode.append("\t\t\t\t\tboolean match = true;\n");
        sourceCode.append("\t\t\t\t\tParameter[] methodParams = m.getParameters();\n");
        sourceCode.append("\t\t\t\t\tfor(int i = 0; i < parameters.length; i++) {\n");
        sourceCode.append("\t\t\t\t\t\tClass<?> paramType = methodParams[i].getType();\n");
        sourceCode.append("\t\t\t\t\t\tparameters[i] = convertToType(parameters[i].toString(), paramType);\n");
        sourceCode.append("\t\t\t\t\t\tif(parameters[i] == null) {\n");
        sourceCode.append("\t\t\t\t\t\t\tmatch = false;\n");
        sourceCode.append("\t\t\t\t\t\t\tbreak;\n");
        sourceCode.append("\t\t\t\t\t\t}\n");
        sourceCode.append("\t\t\t\t\t}\n\n");
        sourceCode.append("\t\t\t\t\tif(match) {\n");
        sourceCode.append("\t\t\t\t\t\tmethod = m;\n");
        sourceCode.append("\t\t\t\t\t\tbreak;\n");
        sourceCode.append("\t\t\t\t\t}\n");
        sourceCode.append("\t\t\t\t}\n");
        sourceCode.append("\t\t\t}\n\n");

        sourceCode.append("\t\t\tif(method == null) {\n");
        sourceCode.append("\t\t\t\tthrow new NoSuchMethodException(\"Method not found: \" + methodName);\n");
        sourceCode.append("\t\t\t}\n");

        sourceCode.append("\t\t\tObject result = method.invoke(serverObject, parameters);\n");
        sourceCode.append("\t\t\treturn new Message(\"").append(interfaceName).append("Server\" , result.toString());\n");
        sourceCode.append("\t\t} catch (Exception e) {\n");
        sourceCode.append("\t\t\te.printStackTrace();\n");
        sourceCode.append("\t\t\treturn new Message(\"").append(interfaceName).append("Server\", \"Error: \" + e.getMessage());\n");
        sourceCode.append("\t\t}\n");
        sourceCode.append("\t}\n\n");

        sourceCode.append("\tprivate Object convertToType(String param, Class<?> targetType) {\n");
        sourceCode.append("\t\tif(targetType == String.class) {\n");
        sourceCode.append("\t\t\treturn param;\n");
        sourceCode.append("\t\t} else if(targetType == Integer.class || targetType == int.class) {\n");
        sourceCode.append("\t\t\treturn Integer.parseInt(param);\n");
        sourceCode.append("\t\t} else if(targetType == Float.class || targetType == float.class) {\n");
        sourceCode.append("\t\t\treturn Float.parseFloat(param);\n");
        sourceCode.append("\t\t}\n\n");
        sourceCode.append("\t\treturn null;\n");
        sourceCode.append("\t}\n");
        sourceCode.append("}\n\n");

        sourceCode.append("public class ").append(interfaceName).append("ServerSideProxy {\n");
        sourceCode.append("\tAddress myAddr = new Entry(\"127.0.0.1\", ").append(port).append(");\n\n");
        sourceCode.append("\tpublic void start() {\n");
        sourceCode.append("\t\tByteStreamTransformer transformer = new ServerTransformer(new ").append(interfaceName).append("Server());\n");
        sourceCode.append("\t\tReplyer r = new Replyer(\"").append(interfaceName).append("Server\", myAddr);\n\n");
        sourceCode.append("\t\twhile(true) {\n");
        sourceCode.append("\t\t\tr.receive_transform_and_send_feedback(transformer);\n");
        sourceCode.append("\t\t}\n");
        sourceCode.append("\t}\n");
        sourceCode.append("}\n");

        String fileName = interfaceName + "ServerSideProxy.java";
        writeToFile(sourceCode.toString(), fileName, "serverproxies");

        if(compile("serverproxies", fileName)) {
            System.out.println("Compilation complete.");
        } else {
            System.out.println("Compilation failed.");
        }
    }

    public static boolean compile(String packageName,  String fileName) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if(compiler == null) {
            System.err.println("Java compiler not available.");
            return false;
        }

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File outputDir = new File("out/production/ToyORB/");
        try {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(outputDir));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        File packageDir = new File("src/" + packageName.replace(".", "/"));
        File[] sourceFile = packageDir.listFiles((dir, name) -> name.equals(fileName));

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourceFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        boolean success = task.call();

        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    private static void writeToFile(String sourceCode, String fileName, String packageName) {
       try{

           String packagePath = "src/" + packageName.replace(".", "/");
           File packageDir = new File(packagePath);
           if (!packageDir.exists()) {
               if (!packageDir.mkdirs()) {
                   System.err.println("Failed to create directory: " + packagePath);
                   return;
               }
           }

           // Create the proxy file
           String filePath = packagePath + "/" + fileName;
           File proxyFile = new File(filePath);
           proxyFile.createNewFile();
           PrintWriter writer = new PrintWriter(new FileWriter(proxyFile));

           writer.print(sourceCode);
           writer.close();
       } catch (Exception e){
           e.printStackTrace();
        }
    }
}