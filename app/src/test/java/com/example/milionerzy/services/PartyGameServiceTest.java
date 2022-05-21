package com.example.milionerzy.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.milionerzy.model.PartyGame;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.model.Team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(MockitoJUnitRunner.class)
public class PartyGameServiceTest {

    private Context context;
    private SharedPreferences sharedPreferences;

    @Mock
    private TeamsListService teamsListService;
    @Mock
    private Question question;
    @Mock
    private Team team;
    @Mock
    private ArrayList<Question> questions;
    @Mock
    private ArrayList<Team> teams;
    @Mock
    private PartyGame partyGame;

    @Before
    public void before() {
        initMocks(this);
        partyGame.setQuestionList(questions);
        sharedPreferences = mock(SharedPreferences.class);
        context = mock(Context.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(partyGame.getQuestionList()).thenReturn(questions);
        when(partyGame.getTeamList()).thenReturn(teams);
    }

    @Test
    void answerIsCorrect_ReturnTrue() {

        PartyGameService partyGameService = new PartyGameService(context);
        partyGameService.setPartyGame(partyGame);
        partyGameService.setTeamsListService(teamsListService);

        when(questions.get(anyInt())).thenReturn(question);
        when(teams.get(anyInt())).thenReturn(team);

        when(question.getCorrectAnswer()).thenReturn("A");

        boolean ansIsCorrect = partyGameService.answerIsCorrect("A");

        assertTrue(ansIsCorrect);
        verify(teamsListService).addPointToTeam(team);
    }


    @Test
    public void getPartyGameDataToDisplay_ReturnOk() {

    }

}