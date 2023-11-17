package com.fatec.feriadoapp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

class Req01ConsultarFeriado {
	
	/*
	 * 
	@Test
	void test() {
		fail("Not yet implemented");
	}
	*
	*/

	@Test
	void ct01ConsultarFeriadoComSucesso() {
		String URL = "https://api.invertexto.com/v1/holidays/2023?token=&state=sp";
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		record Feriado(String date, String nome, String type, String level) {};
		HttpEntity request = new HttpEntity<>(headers);
		ResponseEntity<String> response = template.exchange(URL, HttpMethod.GET, request, String.class);
		assertEquals("200 OK", response.getStatusCode().toString());
		assertEquals("application/json", response.getHeaders().getContentType().toString());
		converteUTF8(response.getBody().toString());
	}
	
	@Test
	void ct02ConsultarFeriadoComAutorizacaoInvalida() {
		String URL = "https://api.invertexto.com/v1/holidays/2023?token=&state=sp";
		ResponseEntity<String> response = null;
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		record Feriado(String date, String nome, String type, String level) {};
		HttpEntity request = new HttpEntity<>(headers);

		try {
			response = template.exchange(URL, HttpMethod.GET, request, String.class);
		}
		catch(HttpClientErrorException e) {
			assertEquals("401 UNAUTHORIZED", e.getStatusCode().toString());
		}
	}
	
	void converteUTF8(String str) {
		Gson gson = new Gson();
		try {
			String listaA = str;
			byte[] listaB = listaA.getBytes("UTF-8");
			String strB = new String(listaB, "UTF-8");
			record Feriado(String name, String type, String level) {};
			Feriado[] lista = gson.fromJson(str, Feriado[].class);
			System.out.println(lista[0]);
			assertEquals(17, lista.length);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	void ct03ConsultarFeriadoComAnoInvalido() {
		ResponseEntity<String> response = null;
		String URL = "https://api.invertexto.com/v1/holidays/?token=&state=sp";
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity request = new HttpEntity<>(headers);
		
		try {
			response = template.exchange(URL, HttpMethod.GET, request, String.class);
		}
		catch(HttpClientErrorException e) {
			assertEquals("404 NOT_FOUND", e.getStatusCode().toString());
		}
	}
}
