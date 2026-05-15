import org.example.studycarddemo.entity.Card;
import org.example.studycarddemo.services.StudyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudyServiceTest {

    private StudyService service;
    private List<Card> testCards;

    @BeforeEach
    void setUp() {
        service = new StudyService();
        testCards = List.of(
                new Card(1, 1, "Q1", "A1"),
                new Card(2, 1, "Q2", "A2"),
                new Card(3, 1, "Q3", "A3")
        );
    }

    @Test
    void startSessionInitializesCorrectly() {
        service.startSession(testCards, false);
        assertEquals(3, service.getTotalCount());
        assertEquals(0, service.getKnownCount());
        assertEquals(0, service.getUnknownCount());
        assertFalse(service.isFinished());
        assertFalse(service.isAnswerRevealed());
    }

    @Test
    void revealAnswerSetsFlag() {
        service.startSession(testCards, false);
        service.revealAnswer();
        assertTrue(service.isAnswerRevealed());
    }

    @Test
    void markKnownAdvancesAndIncrementsCounter() {
        service.startSession(testCards, false);
        Card first = service.getCurrentCard();
        service.markKnown();
        assertEquals(1, service.getKnownCount());
        assertNotEquals(first, service.getCurrentCard());
    }

    @Test
    void sessionFinishesAfterAllCards() {
        service.startSession(testCards, false);
        service.markKnown();
        service.markKnown();
        service.markUnknown();
        assertTrue(service.isFinished());
        assertNull(service.getCurrentCard());
        assertEquals(2, service.getKnownCount());
        assertEquals(1, service.getUnknownCount());
    }

    @Test
    void advancingResetsAnswerRevealedFlag() {
        service.startSession(testCards, false);
        service.revealAnswer();
        service.markKnown();
        assertFalse(service.isAnswerRevealed());
    }
}
