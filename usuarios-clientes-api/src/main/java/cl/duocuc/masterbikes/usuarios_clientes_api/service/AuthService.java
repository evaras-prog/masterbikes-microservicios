package cl.duocuc.masterbikes.usuarios_clientes_api.service;

import cl.duocuc.masterbikes.usuarios_clientes_api.dto.LoginRequest;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.LoginResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.model.Usuario;
import cl.duocuc.masterbikes.usuarios_clientes_api.repository.UsuarioRepository;
import cl.duocuc.masterbikes.usuarios_clientes_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request){

        log.info("Inicio de operación: login para correo {}", request.getCorreo());

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(()-> {
                    log.warn("Login rechazado: no existe usuario con correo {}", request.getCorreo());
                    return new RuntimeException("Credenciales inválidas");
                });

        if (!usuario.getPassword().equals(request.getPassword())){
            log.warn("Login rechazado: password incorrecto para el correo {}", request.getCorreo());
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!"S".equals(usuario.getActivo())){
            log.warn("Login rechazado: usuario inactivo con correo {}", request.getCorreo());
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        String token = jwtUtil.generarToken(usuario.getCorreo());

        log.info("Login exitoso para correo {}", request.getCorreo());

        return new LoginResponse(
                token,
                usuario.getCorreo(),
                usuario.getNombres() + " " + usuario.getApellidos(),
                usuario.getTipoUsuario().getNombre()
        );
    }
}
