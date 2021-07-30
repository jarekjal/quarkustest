package jarekjal;

public class FileConfirmationDTO {

    private String message;
    private String fileName;

    public FileConfirmationDTO(String message, String fileName) {
        this.message = message;
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public String getFileName() {
        return fileName;
    }

}
