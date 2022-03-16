package model;

import exceptions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static model.Formatting.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FormattingTest {

    @Test
    void testFormatting() {
        Formatting testFormatting = new Formatting();
        assertEquals("for junit", testFormatting.getJunit());

    }

    @Test
    void testPretty() {
        assertEquals("", pretty(""));
        assertEquals("testing", pretty("TeStInG"));
        assertEquals("testing", pretty("TeStInG123"));
        assertEquals("testing", pretty("  1 2 T e S t I n G 1 2  "));
    }

    @Test
    void testIsInvalidEntryEmpty() {
        try {
            isValidEntry("");
            fail("String is illegal");
        } catch (IllegalEntryException iee) {
            // pass
        }
    }

    @Test
    void testIsInvalidEntryOnlySpace() {
        try {
            isValidEntry(" ");
            fail("String is illegal");
        } catch (IllegalEntryException iee) {
            // pass
        }
    }

    @Test
    void testIsInvalidEntryLeadingSpace() {
        try {
            isValidEntry(" test");
            fail("String is illegal");
        } catch (IllegalEntryException iee) {
            // pass
        }
    }

    @Test
    void testIsInvalidEntryTrailingSpace() {
        try {
            isValidEntry("test ");
            fail("String is illegal");
        } catch (IllegalEntryException iee) {
            // pass
        }
    }

    @Test
    void testIsInvalidEntryValid() {
        try {
            isValidEntry("test");
            // pass
        } catch (IllegalEntryException iee) {
            fail("String is legal");
        }
    }

    @Test
    void testIsInvalidNameNumbers() {
        try {
            isValidName("123");
            fail("Name is illegal");
        } catch (InvalidNameException ine) {
            // pass
        }
    }

    @Test
    void testIsInvalidNameEmpty() {
        try {
            isValidName("");
            fail("Name is illegal");
        } catch (InvalidNameException ine) {
            // pass
        }
    }

    @Test
    void testIsInvalidNameOnlySpace() {
        try {
            isValidName(" ");
            fail("Name is illegal");
        } catch (InvalidNameException ine) {
            // pass
        }
    }

    @Test
    void testIsInvalidNameNumbersAndLetters() {
        try {
            isValidName("123test");
            fail("Name is illegal");
        } catch (InvalidNameException ine) {
            // pass
        }
    }

    @Test
    void testIsInvalidNameWord() {
        try {
            isValidName("test");
            // pass
        } catch (InvalidNameException ine) {
            fail("Name is legal");
        }
    }

    @Test
    void testIsInvalidNameWordLeadingSpace() {
        try {
            isValidName(" test");
            // pass
        } catch (InvalidNameException ine) {
            fail("Name is legal");
        }
    }

    @Test
    void testIsInvalidNameWordTrailingSpace() {
        try {
            isValidName("test ");
            // pass
        } catch (InvalidNameException ine) {
            fail("Name is legal");
        }
    }

    @Test
    void testIsInvalidNameTwoWords() {
        try {
            isValidName("first last");
            // pass
        } catch (InvalidNameException ine) {
            fail("Name is legal");
        }
    }

    @Test
    void testIsInvalidNameThreeWords() {
        try {
            isValidName("first middle last");
            // pass
        } catch (InvalidNameException ine) {
            fail("Name is legal");
        }
    }

    @Test
    void testIsInvalidNameExtraSpacesWords() {
        try {
            isValidName("first    middle    last");
            // pass
        } catch (InvalidNameException ine) {
            fail("Name is legal");
        }
    }

    @Test
    void testIsValidAmountLetters() {
        try {
            isValidAmount("not numbers");
            fail("Illegal amount");
        } catch (InvalidAmountException iae) {
            // pass
        }
    }

    @Test
    void testIsValidAmountNegativeAmount() {
        try {
            isValidAmount("-50");
            fail("Illegal amount");
        } catch (InvalidAmountException iae) {
            // pass
        }
    }

    @Test
    void testIsValidAmountTwoDecimals() {
        try {
            isValidAmount("10.20.30");
            fail("Illegal amount");
        } catch (InvalidAmountException iae) {
            // pass
        }
    }

    @Test
    void testIsValidAmountProperNoDecimal() {
        try {
            isValidAmount("50");
            // pass
        } catch (InvalidAmountException iae) {
            fail("Amount is legal");
        }
    }

    @Test
    void testIsValidAmountProperDecimal() {
        try {
            isValidAmount("50.10");
            // pass
        } catch (InvalidAmountException iae) {
            fail("Amount is legal");
        }
    }

    @Test
    void testIsValidAmountProperDecimalMore() {
        try {
            isValidAmount("50.1020");
            // pass
        } catch (InvalidAmountException iae) {
            fail("Amount is legal");
        }
    }

    @Test
    void testCapitalizeName() {
        assertEquals("Testing", capitalizeName("testing"));
        assertEquals("Testing Again", capitalizeName("testing again"));
        assertEquals("Testing Three Words", capitalizeName("testing three words"));
        assertEquals("Testing Spaces", capitalizeName("testing      spaces"));
        assertEquals("Testing More Spaces", capitalizeName("testing    mOre  spAces"));
        assertEquals("Testing Capitals", capitalizeName("tESTING cAPITALS"));
        assertEquals("First Middle Last", capitalizeName("   first   midDle lAST"));
    }

    @Test
    void testDoPasswordsMatchMatching() {
        try {
            doPasswordsMatch("matching", "matching");
            // pass
        } catch (PasswordsDoNotMatchException pdnme) {
            fail("Password matches confirmation");
        }
    }

    @Test
    void testDoPasswordsMatchNotMatching() {
        try {
            doPasswordsMatch("matching", "notmatching");
            fail("Password does not match confirmation");
        } catch (PasswordsDoNotMatchException pdnme) {
            // pass
        }
    }

    @Test
    void testDoTransferFromTo() {
        Account testSender = new Account("pass123", "Sender");
        Account testRecipient = new Account("pass123", "Recipient");

        doTransferFromTo("50", testSender, testRecipient);

        assertEquals(new BigDecimal("50"), testSender.getBalance());
        assertEquals(1, testSender.getTransactions().size());
        assertEquals(new BigDecimal("150"), testRecipient.getBalance());
        assertEquals(4, testRecipient.getNotifications().size());
        assertEquals(1, testRecipient.getTransactions().size());
    }

    @Test
    void testCurrencyFormat() {
        BigDecimal testAmount0 = new BigDecimal("0");
        BigDecimal testAmount1 = new BigDecimal("00.50");
        BigDecimal testAmount2 = new BigDecimal("50");
        BigDecimal testAmount3 = new BigDecimal("77.77");
        BigDecimal testAmount4 = new BigDecimal("100.00");

        assertEquals("$0.00", currencyFormat(testAmount0));
        assertEquals("$0.50", currencyFormat(testAmount1));
        assertEquals("$50.00", currencyFormat(testAmount2));
        assertEquals("$77.77", currencyFormat(testAmount3));
        assertEquals("$100.00", currencyFormat(testAmount4));
    }

    @Test
    void testHasSufficientFundsEnough() {
        BigDecimal out = new BigDecimal("5");
        BigDecimal balance = new BigDecimal("10");

        try {
            hasSufficientFunds(out, balance);
            // pass
        } catch (InsufficientFundsException ife) {
            fail("There are enough funds");
        }
    }

    @Test
    void testHasSufficientFundsEnoughBelowBoundary() {
        BigDecimal out = new BigDecimal("9.99");
        BigDecimal balance = new BigDecimal("10");

        try {
            hasSufficientFunds(out, balance);
            // pass
        } catch (InsufficientFundsException ife) {
            fail("There are enough funds");
        }
    }

    @Test
    void testHasSufficientFundsEnoughBoundary() {
        BigDecimal out = new BigDecimal("10");
        BigDecimal balance = new BigDecimal("10");

        try {
            hasSufficientFunds(out, balance);
            // pass
        } catch (InsufficientFundsException ife) {
            fail("There are enough funds");
        }
    }

    @Test
    void testHasSufficientFundsNotEnoughBoundary() {
        BigDecimal out = new BigDecimal("10.01");
        BigDecimal balance = new BigDecimal("10");

        try {
            hasSufficientFunds(out, balance);
            fail("There are not enough funds");
        } catch (InsufficientFundsException ife) {
            // pass
        }
    }

    @Test
    void testHasSufficientFundsNotEnoughGreater() {
        BigDecimal out = new BigDecimal("15");
        BigDecimal balance = new BigDecimal("10");

        try {
            hasSufficientFunds(out, balance);
            fail("There are not enough funds");
        } catch (InsufficientFundsException ife) {
            // pass
        }
    }
}
