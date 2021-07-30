package jarekjal;


import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Path("/hello")
public class GreetingResource {

    @Inject
    MyDependency myDependency;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        Map<String, String> json = new HashMap<>();
        json.put("name", "jarek");
        return json;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public FileConfirmationDTO uploadFile(/*@MultipartForm*/ MultipartFormDataInput input){
        System.out.println("----");
        System.out.println("parts: " + input.getParts().size());
        System.out.println(">>>");
        Map<String, List<InputPart>> formDataMap = input.getFormDataMap();
        int partCount = 0;
        for (String partName : formDataMap.keySet()) {
            System.out.println("part " + partCount++ + ":");
            System.out.println("\tpart name: " + partName);

            formDataMap.get(partName).forEach(part -> {
                try {
                    String originalFileName = getFileNameFromHeader(part.getHeaders().get("Content-Disposition"));
                    InputStream is = part.getBody(InputStream.class, null);
                    byte[] receivedBytes = is.readAllBytes();
                    System.out.println("\toriginalFileName: " + originalFileName);
                    System.out.println("\tbytes: " + receivedBytes.length);
                    System.out.println("\t" + saveFile(receivedBytes, originalFileName));
                    System.out.println("---end of part---");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("--");
        }
        myDependency.setCnt(myDependency.getCnt() + 1);
        return new FileConfirmationExtendedDTO(
                "" + myDependency.getCnt(), myDependency.getMessage(), "info jakies");

    }

    private String saveFile(byte[] receivedBytes, String originalFileName) throws IOException {
        OutputStream out = new FileOutputStream(originalFileName);
        out.write(receivedBytes);
        out.close();
        return "File \"" + originalFileName + "\" saved.";
    }

    private String getFileNameFromHeader(List<String> headers) {
        String result = "fileName_" + UUID.randomUUID();
        if (headers.size() == 1) {
            String header = headers.get(0);
            String[] attributes = header.split("; ");
            List<String> fileNameAttributes = Arrays.asList(attributes).stream().filter(attr -> attr.contains("filename")).collect(Collectors.toList());
            if (fileNameAttributes.size() == 1) {
                String fileNameAttribute = fileNameAttributes.get(0);
                String[] fileNameAttributeSplitted = fileNameAttribute.split("=");
                if (fileNameAttributeSplitted.length == 2) {
                    result = fileNameAttributeSplitted[1].replace("\"", "");
                }
            }
        }
        return result;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String test() {

        return "response";

    }
}