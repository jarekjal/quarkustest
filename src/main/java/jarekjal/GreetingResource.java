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

    private static final boolean SAVE_FILE = false;
    private StringBuilder response = new StringBuilder();

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
    public String uploadFile(/*@MultipartForm*/ MultipartFormDataInput input) {
        if (messageContainsParts(input)) {
            saveCorrespondingPartsToFiles(input.getFormDataMap());
        }
        return response.toString();
    }

    private void saveCorrespondingPartsToFiles(Map<String, List<InputPart>> partNameToPartList) {
        Set<String> partNames = partNameToPartList.keySet();
        for (String partName : partNames) {
            List<InputPart> inputParts = partNameToPartList.get(partName);
            inputParts.forEach(this::savePartAsFile);
        }
    }

    private void savePartAsFile(InputPart part) {
        String originalFileName = getFileName(part.getHeaders().get("Content-Disposition"));
        try {
            InputStream is = part.getBody(InputStream.class, null);
            byte[] receivedBytes = is.readAllBytes();
            response.append("\toriginalFileName: ").append(originalFileName);
            saveFile(receivedBytes, originalFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean messageContainsParts(MultipartFormDataInput input) {
        return input.getParts().size() > 0;
    }

    private void printFileContent(InputPart part) {
        System.out.println("\t---File content: ---");
        try {
            System.out.println(part.getBodyAsString());
        } catch (IOException e) {
            System.out.println("Can't print file content!");
        }
        System.out.println("\t------");
    }

    private void saveFile(byte[] receivedBytes, String originalFileName) throws IOException {
        if (SAVE_FILE) {
            OutputStream out = new FileOutputStream(originalFileName);
            out.write(receivedBytes);
            out.close();
        }
    }

    private String getFileName(List<String> headers) {
        String result = "fileName_" + UUID.randomUUID();
        if (headers.size() == 1) {
            result = extractFileName(headers.get(0)).orElse(result);
        }
        return result;
    }

    private Optional<String> extractFileName(String header) {
        Optional<String> result = Optional.empty();
        String[] attributes = header.split("; ");
        List<String> fileNameAttributes = Arrays.asList(attributes).stream().filter(attr -> attr.contains("filename")).collect(Collectors.toList());
        if (fileNameAttributes.size() == 1) {
            String fileNameAttribute = fileNameAttributes.get(0);
            result = Optional.ofNullable(extractRightSideOfEquation(fileNameAttribute));
        }
        return result;
    }

    private String extractRightSideOfEquation(String equationCandidate) {
        String result;
        try {
            Equation equation = new Equation(equationCandidate);
            result = equation.getRightSide();
        } catch (IllegalArgumentException e) {
            result = "";
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