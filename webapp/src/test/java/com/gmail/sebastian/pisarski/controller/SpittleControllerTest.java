package com.gmail.sebastian.pisarski.controller;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import com.gmail.sebastian.pisarski.builder.SpittleBuilder;
import com.gmail.sebastian.pisarski.controller.SpittleController;
import com.gmail.sebastian.pisarski.entity.Spittle;
import com.gmail.sebastian.pisarski.service.SpittleService;

@RunWith(JUnitParamsRunner.class)
public class SpittleControllerTest {

	@Mock
	private SpittleService spittleService;

	@InjectMocks
	private SpittleController spittleController;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = standaloneSetup(spittleController).build();
	}

	@Test
	public void shouldReturnProperViewWithSpittleListInModel() throws Exception {
		// given
		List<Spittle> spittles = new ArrayList<>();
		when(spittleService.findAllSorted()).thenReturn(spittles);

		// when
		mockMvc.perform(get("/spittle/list"))

				// then
				.andExpect(model().attribute("spittles", spittles))
				.andExpect(view().name("spittle.list"))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldDisplayNewSpittleForm() throws Exception {
		// when
		mockMvc.perform(get("/spittle/new"))

				// then
				.andExpect(model().attribute("spittle", isEmptySpittle()))
				.andExpect(model().attribute("submitBtnCode", "button.create"))
				.andExpect(model().attribute("saveActionUrl", "new"))
				.andExpect(view().name("spittle.new_edit"))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldDisplayEditSpittleForm() throws Exception {
		// given
		long id = 69685L;
		Spittle spittle = new SpittleBuilder().withId(id).build();

		when(spittleService.getById(id)).thenReturn(spittle);

		// when
		mockMvc.perform(get("/spittle/edit/" + id))

				// then
				.andExpect(model().attribute("spittle", spittle))
				.andExpect(model().attribute("submitBtnCode", "button.edit"))
				.andExpect(model().attribute("saveActionUrl", "edit/" + id))
				.andExpect(view().name("spittle.new_edit"))
				.andExpect(status().isOk());
	}

	@Test
	@Parameters(method = "getParameterForShouldSaveSpittle")
	public void shouldSaveSpittle(String url) throws Exception {
		// given
		String title = "title";
		String message = "message";

		// when
		mockMvc.perform(
				post(url).param("title", title).param("message", message))

				// then
				.andExpect(redirectedUrl("/spittle/list"))
				.andExpect(status().is3xxRedirection());

		ArgumentCaptor<Spittle> spittleCaptor = ArgumentCaptor
				.forClass(Spittle.class);
		verify(spittleService).save(spittleCaptor.capture());
		Spittle captured = spittleCaptor.getValue();
		assertEquals(message, captured.getMessage());
		assertEquals(title, captured.getTitle());
	}

	private Matcher<Spittle> isEmptySpittle() {
		return new TypeSafeMatcher<Spittle>() {

			@Override
			public void describeTo(Description description) {
				description
						.appendText("new spittle (all fields with null values)");

			}

			@Override
			protected boolean matchesSafely(Spittle spittle) {
				return spittle.getId() == null && spittle.getTitle() == null
						&& spittle.getMessage() == null
						&& spittle.getTime() == null;
			}
		};
	}

	@SuppressWarnings("unused")
	private Object[] getParameterForShouldSaveSpittle() {
		return $("/spittle/new", "/spittle/edit/54");
	}

}
