package heckmeck.server;

import heckmeck.exceptions.*;

import java.io.Serializable;
import java.util.*;

public class DiceState implements Serializable {

	// Attributes
	private static final long serialVersionUID = -8819803397763601581L;
	private List<Dice> mUnfixedDices;
	private List<Dice> mFixedDices;

	// Constructor
	public DiceState() {

		mUnfixedDices = new ArrayList<Dice>();
		mFixedDices = new ArrayList<Dice>();

		for (int i = 0; i < 8; i++) {
			mUnfixedDices.add(new Dice());
		}
		try {
			dice();
		} catch (MisthrowException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets list of unfixed dices
	 * 
	 * @return
	 */
	public List<Dice> getUnfixedDices() {
		return mUnfixedDices;
	}

	/**
	 * sets list of unfixed dices
	 * 
	 * @param unfixedDices
	 */
	public void setmUnfixedDices(List<Dice> unfixedDices) {
		this.mUnfixedDices = unfixedDices;
	}

	/**
	 * gets list of fixed dices
	 * 
	 * @return
	 */
	public List<Dice> getFixedDices() {
		return mFixedDices;
	}

	/**
	 * set list of fixed dices
	 * 
	 * @param fixedDices
	 */
	public void setmFixedDices(List<Dice> fixedDices) {
		this.mFixedDices = fixedDices;
	}

	/**
	 * returns sum of diced dices
	 * 
	 * @return
	 */
	public int getDicedValue() {

		int result = 0;

		for (Iterator<Dice> iterator = mFixedDices.iterator(); iterator
				.hasNext();) {
			Dice dice = iterator.next();

			result = dice.getValue();
		}

		return result;
	}

	/**
	 * dices all unfixed dices
	 * 
	 * @throws MisthrowException
	 */
	public void dice() throws MisthrowThrowException {

		for (Iterator<Dice> iterator = mUnfixedDices.iterator(); iterator
				.hasNext();) {
			Dice dice = iterator.next();

			dice.dice();
			if (isMisthrow()) {
				throw new MisthrowThrowException();
			}
		}

		sort();
	}

	/**
	 * checks whether a misthrow occured
	 * 
	 * @return
	 */
	private boolean isMisthrow() {
		return validateValueFixed();
	}

	private boolean validateValueFixed() {
		for (Iterator<Dice> iterator = mUnfixedDices.iterator(); iterator
				.hasNext();) {
			Dice dice = iterator.next();

			if (!isValueFixed(dice.getLabel())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * checks whether value is fixed
	 * 
	 * @param value
	 * @return
	 */
	private boolean isValueFixed(String label) {
		for (Iterator<Dice> iterator = mFixedDices.iterator(); iterator
				.hasNext();) {

			Dice dice = iterator.next();

			if (dice.getLabel().equals(label)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * fixes all dices with given value
	 * 
	 * @param value
	 * @throws AlreadyFixedException
	 * @throws ValueNotFoundException
	 */
	public void fixValue(String label) throws AlreadyFixedException,
			ValueNotFoundException {
		boolean valueFound = false;

		if (isValueFixed(label)) {
			throw new AlreadyFixedException();
		}

		for (Iterator<Dice> iterator = mUnfixedDices.iterator(); iterator
				.hasNext();) {
			Dice dice = iterator.next();

			if (dice.getLabel().equals(label)) {
				mFixedDices.add(dice);
				valueFound = true;
			}
		}

		mUnfixedDices.removeAll(mFixedDices);
		sort();

		if (!valueFound) {
			throw new ValueNotFoundException();
		}
	}

	/**
	 * clears list of fixed dices and resets dicestate
	 */
	public void clear() {

		mUnfixedDices.addAll(mFixedDices);
		mFixedDices.clear();

		assert (mUnfixedDices.size() == 8);
	}

	@Override
	public String toString() {
		StringBuilder sB = new StringBuilder();
		List<Dice> fixedDices = getFixedDices();
		List<Dice> unfixedDices = getUnfixedDices();

		sB.append("Fixed: ");
		for (Iterator<Dice> iterator = fixedDices.iterator(); iterator
				.hasNext();) {
			Dice dice = iterator.next();
			sB.append(dice.getLabel() + " ");
		}
		sB.append("\nUnfixed: ");
		for (Iterator<Dice> iterator = unfixedDices.iterator(); iterator
				.hasNext();) {
			Dice dice = iterator.next();
			sB.append(dice.getLabel() + " ");
		}
		return sB.toString();
	}

	/**
	 * sorts lists of fixed and unfixed dices
	 */
	private void sort() {
		Collections.sort(mFixedDices);
		Collections.sort(mUnfixedDices);
	}
}
