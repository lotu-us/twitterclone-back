package com.example.twitterclone.controller;

import com.example.twitterclone.dto.BoardDTO;
import com.example.twitterclone.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity saveBoard(@Validated @RequestBody BoardDTO.Insert boardDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){  //입력값 체크
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult);   //하단 주석 참고
        }
        Long boardId = boardService.saveBoard(boardDTO);
        return ResponseEntity.status(HttpStatus.OK).body(boardId);
    }

    @GetMapping("/boards")
    public ResponseEntity getBoards(HttpServletRequest request){
        System.out.println("클라이언트 IP : "+request.getRemoteAddr());
        List<BoardDTO.Response> boards = boardService.getBoards();
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    @PutMapping("/board/{boardId}")
    public ResponseEntity updateBoard(@PathVariable Long boardId, @Validated @RequestBody BoardDTO.Update boardDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){  //입력값 체크
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult);
        }
        boardService.updateBoard(boardId, boardDTO);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId, @Validated @RequestBody BoardDTO.Delete boardDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){  //입력값 체크
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult);
        }
        boardService.deleteBoard(boardId, boardDTO);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }






    // testException = org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.springframework.http.converter.HttpMessageConversionException: Type definition error: [simple type, class org.springframework.validation.DefaultMessageCodesResolver]; nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.springframework.validation.DefaultMessageCodesResolver and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: org.springframework.validation.BeanPropertyBindingResult["messageCodesResolver"]), mergedContextConfiguration = [WebMergedContextConfiguration@232024b9 testClass = BoardApiControllerTest, locations = '{}', classes = '{class com.example.twitterclone.TwittercloneApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.autoconfigure.actuate.metrics.MetricsExportContextCustomizerFactory$DisableMetricExportContextCustomizer@78a287ed, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@4b3fa0b3, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@4de41af9, [ImportsContextCustomizer@55a8dc49 key = [org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration, org.springframework.boot.test.autoconfigure.web.servlet.MockMvcWebClientAutoConfiguration, org.springframework.boot.test.autoconfigure.web.servlet.MockMvcWebDriverAutoConfiguration, org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration, org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration, org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration, org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration, org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration, org.springframework.boot.test.autoconfigure.web.servlet.MockMvcSecurityConfiguration, org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration]], org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2b491fee, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@5ddabb18, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@71ae31b0, org.springframework.boot.test.context.SpringBootTestArgs@1, org.springframework.boot.test.context.SpringBootTestWebEnvironment@189cbd7c], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]], attributes = map['org.springframework.test.context.web.ServletTestExecutionListener.activateListener' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.populatedRequestContextHolder' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.resetRequestContextHolder' -> true, 'org.springframework.test.context.event.ApplicationEventsTestExecutionListener.recordApplicationEvents' -> false, 'org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener.mocks' -> org.mockito.internal.configuration.InjectingAnnotationEngine$$Lambda$1333/0x000000010094a040@7ca7bddd]]
    //serialization없이 직접 bindingResult를 body에 내려주면 위와같은 에러 발생함
    /*
    * return ResponseEntity.ok().body(객체) 가 있을 때에 body에 일반적인 객체를 넣으면 알아서 json 형태로 변환이 된다.

    ObjectMapper(다양한 Serializer를 가지고 있음)가 자바 빈 스펙을 따르는 객체의 경우에 Bean Serializer를 이용해서 변환해주기 때문이다.
    하지만 Errors 객체는 예외이다.
    Errors는 자바 빈 스펙을 준수하지 않기 때문에 ObjectMapper에서 Bean Serializer를 통해 Serialization이 불가능하다.

    그래서 따로 Serializer를 만든 후에 ObjectMapper에 등록을 해주면 Errors를 Serialization 해줄 수 있게 된다.
    * */
}

