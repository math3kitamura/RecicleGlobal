package br.com.fiap.recicleglobal.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;

@RestControllerAdvice
public class TratadorDeExcecoes {
    public record ErroApi(String erro, List<String> detalhes) {}

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroApi> naoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroApi(ex.getMessage(), List.of()));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroApi> regraNegocio(RegraNegocioException ex) {
        return ResponseEntity.badRequest().body(new ErroApi(ex.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApi> camposInvalidos(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(new ErroApi("Dados inválidos.", detalhes));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroApi> formatoInvalido(Exception ex) {
        return ResponseEntity.badRequest().body(new ErroApi(
                "Formato inválido: verifique o JSON e os valores dos filtros.", List.of()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroApi> conflitoDeDados(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroApi(
                "Conflito no banco de dados: email repetido ou registro com vínculos.", List.of()));
    }
}
