package usuarios.dtos;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private boolean enabled;
    private Long fechaNacimiento;
}
