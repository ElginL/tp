package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOODS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.address.model.transaction.Date.DEFAULT_PATTERN;
import static seedu.address.model.transaction.Date.NEW_PATTERN;

import java.time.LocalDate;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SellCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.transaction.Date;
import seedu.address.model.transaction.Goods;
import seedu.address.model.transaction.Price;
import seedu.address.model.transaction.Quantity;
import seedu.address.model.transaction.SellTransaction;
import seedu.address.model.transaction.Transaction;


/**
 * Parses input arguments and creates a new {@code SellCommand} object
 */
public class SellCommandParser implements Parser<SellCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code SellCommand}
     * and returns a {@code SellCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SellCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_QUANTITY, PREFIX_GOODS, PREFIX_PRICE,
                PREFIX_DATE);
        boolean isDateEmpty = argMultimap.getValue(PREFIX_DATE).equals(Optional.empty());

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SellCommand.MESSAGE_USAGE), ive);
        }

        if (argMultimap.getValue(PREFIX_GOODS).isEmpty() || argMultimap.getValue(PREFIX_PRICE).isEmpty()
                || argMultimap.getValue(PREFIX_QUANTITY).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SellCommand.MESSAGE_USAGE));
        }

        Goods goods = ParserUtil.parseGoods(argMultimap.getValue(PREFIX_GOODS).orElse(""));
        Price price = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_PRICE).orElse(""));
        Quantity quantity = ParserUtil.parseQuantity(argMultimap.getValue(PREFIX_QUANTITY).orElse(""));
        Date date;
        if (isDateEmpty) {
            LocalDate now = LocalDate.now();
            LocalDate datetime = LocalDate.parse(now.toString(), DEFAULT_PATTERN);
            String output = datetime.format(NEW_PATTERN);
            date = new Date(output);
        } else {
            date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).orElse(""));
        }

        Transaction transaction = new SellTransaction(goods, price, quantity, date);

        return new SellCommand(index, transaction);
    }
}
