package jarekjal;

public class FileConfirmationExtendedDTO extends FileConfirmationDTO{

    private String addInfo;

    public FileConfirmationExtendedDTO(String message, String fileName, String addInfo) {
        super(message, fileName);
        this.addInfo = addInfo;
    }

    public String getAddInfo() {
        return addInfo;
    }

    @Override
    public String toString() {
        return "FileConfirmationExtendedDTO{" +
                "addInfo='" + addInfo + '\'' +
                "base class: " + super.getFileName() +  " " + super.getMessage() +
                '}';
    }
}
