package ilay.bar.uno;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import java.util.Collections;
import java.util.Random;

import ilay.bar.uno.Model.Card;
import ilay.bar.uno.Model.Deck;

public class RainingCardsThread extends Thread {
    private final View view;
    private boolean isRunning = true;
    private Random random;

    public RainingCardsThread(View view) {
        this.view = view;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (isRunning) {
            // Generate a new card image and add it to the view
            ImageView card = new ImageView(view.getContext());
            Card cardRandom = getRandomCard();
            int resourceId = getResourceIdForCard(cardRandom);
            card.setImageResource(resourceId);
            card.setLayoutParams(new ViewGroup.LayoutParams(250, 250));

            // Set the card's initial position to be off the top of the screen
            int x = new Random().nextInt(Math.max(1, view.getWidth() - card.getWidth()));
            int y = -card.getHeight();
            card.setX(x);
            card.setY(y);

            // Animate the card falling from the top to the bottom of the screen
            ObjectAnimator animator = ObjectAnimator.ofFloat(card, "y", y, view.getHeight());
            animator.setDuration(2000);
            animator.setInterpolator(new AccelerateInterpolator());

            // Add the card to the view on the main thread
            view.post(() -> {
                ((ViewGroup) view).addView(card);
                animator.start();
            });

            // Wait a random amount of time before generating the next card
            try {
                Thread.sleep(new Random().nextInt(250));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRunning() {
        isRunning = false;
    }

    public Card getRandomCard() {
        Card.Colors[] colors = Card.ColorValues;
        int randomValue = random.nextInt(13) + 1;
        int randomColorIndex = random.nextInt(colors.length);
        Card.Colors randomColor = colors[randomColorIndex];

        Card card = new Card(randomColor, randomValue, false);
        card.setFaceUp(true);

        return card;
    }

    public int getResourceIdForCard(Card card) {
        String resourceName = card.getColor().toString().toLowerCase() + "_" + card.getValue();
        return view.getResources().getIdentifier(resourceName, "drawable", view.getContext().getPackageName());
    }
}
