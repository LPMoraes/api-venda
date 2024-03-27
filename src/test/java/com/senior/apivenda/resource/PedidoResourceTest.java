package com.senior.apivenda.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senior.apivenda.model.Pedido;
import com.senior.apivenda.model.dto.PedidoSaidaDto;
import com.senior.apivenda.model.dto.ProdutoDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.service.PedidoService;
import org.jeasy.random.EasyRandom;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PedidoResourceTest {
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<Pedido> pagePedido;
    @Mock
    PedidoService pedidoService;
    @InjectMocks
    PedidoResource pedidoResource;
    private MockMvc mvc;
    private static ObjectMapper objectMapper;
    private EasyRandom easyRandom;
    public static final String END_POINT = "/pedidos";
    public static String UUID_STR;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(pedidoResource).build();
        objectMapper = new ObjectMapper();
        easyRandom = new EasyRandom();
        UUID_STR =  easyRandom.nextObject(UUID.class).toString();
    }

    @Test
    void testBuscar() throws Exception {
        pageableMock = PageRequest.of(0,10);
        List<Pedido> pedidos = easyRandom.objects(Pedido.class, 7).collect(Collectors.toList());
        pagePedido = new PageImpl(pedidos, pageableMock, pedidos.size());

        when(pedidoService.buscarTodos(pageableMock)).thenReturn(pagePedido);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(END_POINT)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse()).isNotNull();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertThat(jsonObject.get("totalPages")).isEqualTo(1);
        assertThat(jsonObject.get("totalElements")).isEqualTo(7);
        assertThat(jsonObject.get("number")).isEqualTo(0);
        assertThat(jsonObject.get("size")).isEqualTo(10);
        assertThat(jsonObject.get("content")).isNotNull();
    }

    @Test
    void testBuscarPorId() throws Exception {
        when(pedidoService.buscarPorId(anyString())).thenReturn(new PedidoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(END_POINT+"/"+UUID_STR)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PedidoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PedidoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new PedidoSaidaDto(), objResult);
    }

    @Test
    void testCriar() throws Exception {
        when(pedidoService.salvar(any())).thenReturn(new PedidoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post(END_POINT)
                        .content(objectMapper.writeValueAsString(new ProdutoDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        PedidoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PedidoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new PedidoSaidaDto(), objResult);
    }

    @Test
    void testAtualizar() throws Exception {
        when(pedidoService.atualizar(anyString(), any())).thenReturn(new PedidoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put(END_POINT+"/"+UUID_STR)
                        .content(objectMapper.writeValueAsString(new ProdutoDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PedidoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PedidoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new PedidoSaidaDto(), objResult);
    }

    @Test
    void testRemover() throws Exception {
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        String messageSucess = String.format("Pedido uuid:'%s' removido com sucesso", UUID_STR);
        RespostaDto reposta = RespostaDto.sucess(messageSucess, httpStatus);

        when(pedidoService.remover(anyString())).thenReturn(reposta);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .delete(END_POINT+"/"+UUID_STR)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();

        RespostaDto resultResposta = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RespostaDto.class);
        Assertions.assertEquals(String.valueOf(httpStatus.value()), resultResposta.getStatus());
        Assertions.assertEquals(messageSucess, resultResposta.getMessage());
    }
}

