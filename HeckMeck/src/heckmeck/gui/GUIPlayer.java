package heckmeck.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import heckmeck.server.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class GUIPlayer extends GUIBackground {

	// Attributes
	private static final long serialVersionUID = -8444188031674557590L;
	private static final int mTextSize = 15;
	private JLabel mNameLabel;
	private JLabel mAmountTokenLabel;
	private JLabel mAmountDots;
	private JPanel mFixedDicesPanel;
	private JPanel mTopTokenPanel;
	private JPanel mLabelPanel;

	// Constructor
	public GUIPlayer() {
		initUI();
		setLayout(new GridLayout(3, 1));
		setSize(new Dimension(500, 300));
		setOpaque(true);
	}

	public void setPlayerState(PlayerState playerState) {
		setLabels(playerState);
		markCurrentPlayer(playerState.isTurn());
		setTopToken(playerState.getDeck().getTopToken());
		setFixedDices(playerState.getDiceState().getFixedDices());
	}

	private void setLabels(PlayerState playerState) {
		mNameLabel.setText(playerState.getName());
		mNameLabel.setFont(mNameLabel.getFont()
				.deriveFont(Font.BOLD, mTextSize));
		mNameLabel.setForeground(Color.WHITE);
		mLabelPanel.add(mNameLabel);

		int amountToken = playerState.getDeck().getSize();
		mAmountTokenLabel.setText("Tokens: " + String.valueOf(amountToken));
		mAmountTokenLabel.setFont(mAmountTokenLabel.getFont().deriveFont(
				Font.BOLD, mTextSize));
		mAmountTokenLabel.setForeground(Color.WHITE);
		mLabelPanel.add(mAmountTokenLabel);

		int amountDots = getAmountDots(playerState);
		mAmountDots.setText("Amount Dots: " + String.valueOf(amountDots));
		mAmountDots.setFont(mAmountDots.getFont().deriveFont(Font.BOLD,
				mTextSize));
		mAmountDots.setForeground(Color.WHITE);
		mLabelPanel.add(mAmountDots);
	}

	private int getAmountDots(PlayerState playerState) {
		int amount = 0;

		for (int i = 0; i < playerState.getDiceState().getFixedDices().size(); i++) {
			Dice dice = playerState.getDiceState().getFixedDices().get(i);
			amount += dice.getValue();
		}

		return amount;
	}

	private void initUI() {
		mNameLabel = new JLabel();
		mAmountTokenLabel = new JLabel();
		mAmountDots = new JLabel();

		mLabelPanel = new JPanel(new GridLayout(3, 1));
		mLabelPanel.setOpaque(false);
		mFixedDicesPanel = new JPanel();
		mFixedDicesPanel.setOpaque(false);
		mTopTokenPanel = new JPanel();
		mTopTokenPanel.setOpaque(false);

		add(mLabelPanel);
		add(mFixedDicesPanel);
		add(mTopTokenPanel);
		revalidate();
		repaint();
	}

	private void setTopToken(Token topToken) {
		mTopTokenPanel.removeAll();

		if (topToken == null) {
			return;
		}

		String path = "/heckmeck/pictures/" + topToken.getValue() + ".png";
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
		GUIGame.resizeImageIcon(imageIcon);
		mTopTokenPanel.add(new JLabel(imageIcon));
		revalidate();
		repaint();
	}

	private void setFixedDices(List<Dice> fixedDices) {
		mFixedDicesPanel.removeAll();

		for (int i = 0; i < fixedDices.size(); i++) {
			Dice dice = fixedDices.get(i);
			String path = "/heckmeck/pictures/W" + dice.getLabel() + ".png";
			ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
			GUIGame.resizeImageIcon(imageIcon);
			mFixedDicesPanel.add(new JLabel(imageIcon));
			revalidate();
			repaint();
		}
	}

	private void markCurrentPlayer(boolean isTurn) {
		if (isTurn) {
			Border margin = new LineBorder(Color.WHITE, 4);
			setBorder(new CompoundBorder(null, margin));
		} else {
			setBorder(null);
		}
	}
}