/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.javafx.scene.control.skin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.sun.javafx.scene.control.behavior.BehaviorBase;

public class SplitPaneSkin extends BehaviorSkinBase<SplitPane, BehaviorBase<SplitPane>>  {

    private ObservableList<Content> contentRegions;
    private ObservableList<ContentDivider> contentDividers;
    private boolean horizontal;
    
    public SplitPaneSkin(final SplitPane splitPane) {
        super(splitPane, new BehaviorBase<>(splitPane, Collections.EMPTY_LIST));
//        splitPane.setManaged(false);
        horizontal = getSkinnable().getOrientation() == Orientation.HORIZONTAL;
        
        contentRegions = FXCollections.<Content>observableArrayList();
        contentDividers = FXCollections.<ContentDivider>observableArrayList();

        int index = 0;
        for (Node n: getSkinnable().getItems()) {
            addContent(index++, n);
        }
        initializeContentListener();

        for (SplitPane.Divider d: getSkinnable().getDividers()) {
            addDivider(d);
        }

        registerChangeListener(splitPane.orientationProperty(), "ORIENTATION");
        registerChangeListener(splitPane.widthProperty(), "WIDTH");
        registerChangeListener(splitPane.heightProperty(), "HEIGHT");
    }

    private void addContent(int index, Node n) {
        Content c = new Content(n);
        contentRegions.add(index, c);
        getChildren().add(index, c);
    }

    private void removeContent(Node n) {
        for (Content c: contentRegions) {
            if (c.getContent().equals(n)) {
                getChildren().remove(c);
                contentRegions.remove(c);
                break;
            }
        }
    }

    private void initializeContentListener() {
        getSkinnable().getItems().addListener(new ListChangeListener<Node>() {
            @Override public void onChanged(Change<? extends Node> c) {
                while (c.next()) {
                    if (c.wasPermutated() || c.wasUpdated()) {
                        /**
                         * the contents were either moved, or updated.
                         * rebuild the contents to re-sync
                         */
                        getChildren().clear();
                        contentRegions.clear();
                        int index = 0;
                        for (Node n : c.getList()) {
                            addContent(index++, n);
                        }

                    } else {
                        for (Node n : c.getRemoved()) {
                            removeContent(n);
                        }

                        int index = c.getFrom();
                        for (Node n : c.getAddedSubList()) {
                            addContent(index++, n);
                        }
                    }
                }
                // TODO there may be a more efficient way than rebuilding all the dividers
                // everytime the list changes.
                removeAllDividers();
                for (SplitPane.Divider d: getSkinnable().getDividers()) {
                    addDivider(d);
                }
            }
        });
    }
          
    // This listener is to be removed from 'removed' dividers and added to 'added' dividers
    class PosPropertyListener implements ChangeListener<Number> {
        ContentDivider divider;
        
        public PosPropertyListener(ContentDivider divider) {
            this.divider = divider;
        }
        
        @Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {  
            // If we already know the dividers are in the correct position.  We do not
            // need to recheck their values.
            if (checkDividerPos) {
                checkDividerPosition(divider, posToDividerPos(divider, newValue.doubleValue()), posToDividerPos(divider, oldValue.doubleValue()));                
            }
            getSkinnable().requestLayout();
        }
    }
       
    private void checkDividerPosition(ContentDivider divider, double newPos, double oldPos) {
        double dividerWidth = divider.prefWidth(-1);
        Content left = getLeft(divider);
        Content right = getRight(divider);
        double minLeft = left == null ? 0 : (horizontal) ? left.minWidth(-1) : left.minHeight(-1);
        double minRight = right == null ? 0 : (horizontal) ? right.minWidth(-1) : right.minHeight(-1);
        double maxLeft = left == null ? 0 :
            left.getContent() != null ? (horizontal) ? left.getContent().maxWidth(-1) : left.getContent().maxHeight(-1) : 0;
        double maxRight = right == null ? 0 :
            right.getContent() != null ? (horizontal) ? right.getContent().maxWidth(-1) : right.getContent().maxHeight(-1) : 0;                        
               
        double previousDividerPos = 0;
        double nextDividerPos = getSize();
        int index = contentDividers.indexOf(divider);

        if (index - 1 >= 0) {
            previousDividerPos = contentDividers.get(index - 1).getDividerPos();
            if (previousDividerPos == -1) {
                // Get the divider position if it hasn't been initialized.
                previousDividerPos = getAbsoluteDividerPos(contentDividers.get(index - 1));
            }
        }
        if (index + 1 < contentDividers.size()) {
            nextDividerPos = contentDividers.get(index + 1).getDividerPos();
            if (nextDividerPos == -1) {
                // Get the divider position if it hasn't been initialized.
                nextDividerPos = getAbsoluteDividerPos(contentDividers.get(index + 1));
            }
        }
        
        // Set the divider into the correct position by looking at the max and min content sizes.
        checkDividerPos = false;
        if (newPos > oldPos) {
            double max = previousDividerPos == 0 ? maxLeft : previousDividerPos + dividerWidth + maxLeft;
            double min = nextDividerPos - minRight - dividerWidth;                
            double stopPos = Math.min(max, min);
            if (newPos >= stopPos) {                
                setAbsoluteDividerPos(divider, stopPos);
            } else {
                double rightMax = nextDividerPos - maxRight - dividerWidth;                    
                if (newPos <= rightMax) {
                    setAbsoluteDividerPos(divider, rightMax);
                } else {                    
                    setAbsoluteDividerPos(divider, newPos);
                }
            }             
        } else {
            double max = nextDividerPos - maxRight - dividerWidth;
            double min = previousDividerPos == 0 ? minLeft : previousDividerPos + minLeft + dividerWidth;
            double stopPos = Math.max(max, min);
            if (newPos <= stopPos) {
                setAbsoluteDividerPos(divider, stopPos);
            } else {
                double leftMax = previousDividerPos + maxLeft + dividerWidth;
                if (newPos >= leftMax) {
                    setAbsoluteDividerPos(divider, leftMax);
                } else {
                    setAbsoluteDividerPos(divider, newPos);
                }
            }                
        }                    
        checkDividerPos = true;
    }
    
    private void addDivider(SplitPane.Divider d) {
        ContentDivider c = new ContentDivider(d);        
        c.setInitialPos(d.getPosition());
        c.setDividerPos(-1);
        ChangeListener<Number> posPropertyListener = new PosPropertyListener(c);
        c.setPosPropertyListener(posPropertyListener);
        d.positionProperty().addListener(posPropertyListener);
        initializeDivderEventHandlers(c);
        contentDividers.add(c);
        getChildren().add(c);
    }

    private void removeAllDividers() {
        ListIterator<ContentDivider> dividers = contentDividers.listIterator();
        while (dividers.hasNext()) {
            ContentDivider c = dividers.next();
            getChildren().remove(c);
            c.getDivider().positionProperty().removeListener(c.getPosPropertyListener());
            dividers.remove();
        }
        lastDividerUpdate = 0;
    }

    private void initializeDivderEventHandlers(final ContentDivider divider) {
        // TODO: do we need to consume all mouse events?
        // they only bubble to the skin which consumes them by default
        divider.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                event.consume();
            }
        });

        divider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                if (horizontal) {
                    divider.setInitialPos(divider.getDividerPos());
                    divider.setPressPos(e.getSceneX());
                    divider.setPressPos(getSkinnable().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT
                            ? getSkinnable().getWidth() - e.getSceneX() : e.getSceneX());
                } else {
                    divider.setInitialPos(divider.getDividerPos());
                    divider.setPressPos(e.getSceneY());
                }
                e.consume();
            }
        });

        divider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {                
                double delta = 0; 
                if (horizontal) {
                    delta = getSkinnable().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT 
                            ? getSkinnable().getWidth() - e.getSceneX() : e.getSceneX();
                } else {
                    delta = e.getSceneY();
                }
                delta -= divider.getPressPos();
                double newPos = Math.ceil(divider.getInitialPos() + delta);    
                checkDividerPos = true;
                setAbsoluteDividerPos(divider, newPos);
                e.consume();
            }
        });
    }

    private Content getLeft(ContentDivider d) {
        int index = contentDividers.indexOf(d);
        if (index != -1) {
            return contentRegions.get(index);
        }
        return null;
    }

    private Content getRight(ContentDivider d) {
        int index = contentDividers.indexOf(d);
        if (index != -1) {
            return contentRegions.get(index + 1);
        }
        return null;
    }

    @Override protected void handleControlPropertyChanged(String property) {
        super.handleControlPropertyChanged(property);
        if ("ORIENTATION".equals(property)) {
            this.horizontal = getSkinnable().getOrientation() == Orientation.HORIZONTAL;
            for (ContentDivider c: contentDividers) {
                c.setGrabberStyle(horizontal);
            }
            getSkinnable().requestLayout();
        } else if ("WIDTH".equals(property) || "HEIGHT".equals(property)) {
            getSkinnable().requestLayout();
        }
    }

    // Value is the left edge of the divider
    private void setAbsoluteDividerPos(ContentDivider divider, double value) {
        if (getSkinnable().getWidth() > 0 && getSkinnable().getHeight() > 0 && divider != null) {
            SplitPane.Divider paneDivider = divider.getDivider();
            divider.setDividerPos(value);
            double size = getSize();
            if (size != 0) {
                // Adjust the position to the center of the
                // divider and convert its position to a percentage.
                double pos = value + divider.prefWidth(-1)/2;
                paneDivider.setPosition(pos / size);
            } else {
                paneDivider.setPosition(0);
            }
        }
    }
   
    // Updates the divider with the SplitPane.Divider's position
    // The value updated to SplitPane.Divider will be the center of the divider.
    // The returned position will be the left edge of the divider
    private double getAbsoluteDividerPos(ContentDivider divider) {
        if (getSkinnable().getWidth() > 0 && getSkinnable().getHeight() > 0 && divider != null) {
            SplitPane.Divider paneDivider = divider.getDivider();
            double newPos = posToDividerPos(divider, paneDivider.getPosition());
            divider.setDividerPos(newPos);
            return newPos;
        }
        return 0;
    }

    // Returns the left edge of the divider at pos
    // Pos is the percentage location from SplitPane.Divider.
    private double posToDividerPos(ContentDivider divider, double pos) {
        double newPos = getSize() * pos;
        if (pos == 1) {                
            newPos -= divider.prefWidth(-1);
        } else {
            newPos -= divider.prefWidth(-1)/2;
        }       
        return Math.round(newPos);                    
    }
    
    private double totalMinSize() {
        double dividerWidth = !contentDividers.isEmpty() ? contentDividers.size() * contentDividers.get(0).prefWidth(-1) : 0;
        double minSize = 0;
        for (Content c: contentRegions) {
            if (horizontal) {
                minSize += c.minWidth(-1);
            } else {
                minSize += c.minHeight(-1);
            }
        }
        return minSize + dividerWidth;
    }

    private double getSize() {
        final SplitPane s = getSkinnable();
        double size = totalMinSize();
        if (horizontal) {
            if (s.getWidth() > size) {
                size = s.getWidth() - snappedLeftInset() - snappedRightInset();
            }
        } else {
            if (s.getHeight() > size) {
                size = s.getHeight() - snappedTopInset() - snappedBottomInset();
            }
        }
        return size;
    }

    // Evenly distribute the size to the available list.
    // size is the amount to distribute.
    private double distributeTo(List<Content> available, double size) {
        if (available.isEmpty()) {
            return size;
        }
                
        size = snapSize(size);
        int portion = (int)(size)/available.size();
        int remainder;

        while (size > 0 && !available.isEmpty()) {
            Iterator<Content> i = available.iterator();
            while (i.hasNext()) {
                Content c = i.next();
                double max = Math.min((horizontal ? c.maxWidth(-1) : c.maxHeight(-1)), Double.MAX_VALUE);
                double min = horizontal ? c.minWidth(-1) : c.minHeight(-1);

                // We have too much space
                if (c.getArea() >= max) {
                    c.setAvailable(c.getArea() - min);
                    i.remove();
                    continue;
                }
                // Not enough space
                if (portion >= (max - c.getArea())) {
                    size -= (max - c.getArea());
                    c.setArea(max);
                    c.setAvailable(max - min);
                    i.remove();
                } else {
                    // Enough space
                    c.setArea(c.getArea() + portion);
                    c.setAvailable(c.getArea() - min);
                    size -= portion;
                }
                if ((int)size == 0) {
                    return size;
                }
            }
            if (available.isEmpty()) {
                // We reached the max size for everything just return
                return size;
            }
            portion = (int)(size)/available.size();
            remainder = (int)(size)%available.size();
            if (portion == 0 && remainder != 0) {
                portion = remainder;
                remainder = 0;
            }
        }
        return size;
    }

    // Evenly distribute the size from the available list.
    // size is the amount to distribute.
    private double distributeFrom(double size, List<Content> available) {
        if (available.isEmpty()) {
            return size;
        }
        
        size = snapSize(size);
        int portion = (int)(size)/available.size();
        int remainder;

        while (size > 0 && !available.isEmpty()) {
            Iterator<Content> i = available.iterator();
            while (i.hasNext()) {
                Content c = i.next();
                //not enough space taking available and setting min
                if (portion >= c.getAvailable()) {
                    c.setArea(c.getArea() - c.getAvailable()); // Min size
                    size -= c.getAvailable();
                    c.setAvailable(0);
                    i.remove();
                } else {
                    //enough space
                    c.setArea(c.getArea() - portion);
                    c.setAvailable(c.getAvailable() - portion);
                    size -= portion;
                }
                if ((int)size == 0) {
                    return size;
                }
            }
            if (available.isEmpty()) {
                // We reached the min size for everything just return
                return size;
            }
            portion = (int)(size)/available.size();
            remainder = (int)(size)%available.size();
            if (portion == 0 && remainder != 0) {
                portion = remainder;
                remainder = 0;
            }
        }
        return size;
    }

    private void setupContentAndDividerForLayout() {
        // Set all the value to prepare for layout
        double dividerWidth = contentDividers.isEmpty() ? 0 : contentDividers.get(0).prefWidth(-1);
        double startX = 0;
        double startY = 0;
        for (Content c: contentRegions) {
            if (resize && !c.isResizableWithParent()) {
                c.setArea(c.getResizableWithParentArea());
            }
            
            c.setX(startX);
            c.setY(startY);
            if (horizontal) {
                startX += (c.getArea() + dividerWidth);
            } else {
                startY += (c.getArea() + dividerWidth);
            }
        }

        startX = 0;
        startY = 0;
        // The dividers are already in the correct positions.  Disable
        // checking the divider positions.
        checkDividerPos = false;
        for (int i = 0; i < contentDividers.size(); i++) {
            ContentDivider d = contentDividers.get(i);
            if (horizontal) {
                startX += getLeft(d).getArea() + (i == 0 ? 0 : dividerWidth);
            } else {
                startY += getLeft(d).getArea() + (i == 0 ? 0 : dividerWidth);
            }
            d.setX(startX);
            d.setY(startY);              
            setAbsoluteDividerPos(d, (horizontal ? d.getX() : d.getY()));
        }
        checkDividerPos = true;
    }

    private void layoutDividersAndContent(double width, double height) {
        final double paddingX = snappedLeftInset();
        final double paddingY = snappedTopInset();
        final double dividerWidth = contentDividers.isEmpty() ? 0 : contentDividers.get(0).prefWidth(-1);

        for (Content c: contentRegions) {
//            System.out.println("LAYOUT " + c.getId() + " PANELS X " + c.getX() + " Y " + c.getY() + " W " + (horizontal ? c.getArea() : width) + " H " + (horizontal ? height : c.getArea()));
            if (horizontal) {
                c.setClipSize(c.getArea(), height);
                layoutInArea(c, c.getX() + paddingX, c.getY() + paddingY, c.getArea(), height,
                    0/*baseline*/,HPos.CENTER, VPos.CENTER);
            } else {
                c.setClipSize(width, c.getArea());
                layoutInArea(c, c.getX() + paddingX, c.getY() + paddingY, width, c.getArea(),
                    0/*baseline*/,HPos.CENTER, VPos.CENTER);
            }
        }
        for (ContentDivider c: contentDividers) {
//            System.out.println("LAYOUT DIVIDERS X " + c.getX() + " Y " + c.getY() + " W " + (horizontal ? dividerWidth : width) + " H " + (horizontal ? height : dividerWidth));
            if (horizontal) {
                c.resize(dividerWidth, height);
                positionInArea(c, c.getX() + paddingX, c.getY() + paddingY, dividerWidth, height,
                    /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
            } else {
                c.resize(width, dividerWidth);                
                positionInArea(c, c.getX() + paddingX, c.getY() + paddingY, width, dividerWidth,
                    /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
            }
        }
    }

    private double previousArea = -1;
    private double previousWidth = -1;
    private double previousHeight = -1;
    private int lastDividerUpdate = 0;
    private boolean resize = false;
    private boolean checkDividerPos = true;

    @Override protected void layoutChildren(final double x, final double y,
            final double w, final double h) {
        final SplitPane s = getSkinnable();
        final double sw = s.getWidth();
        final double sh = s.getHeight();
        
        if (!s.isVisible() || 
            (horizontal ? sw == 0 : sh == 0) ||
            contentRegions.isEmpty()) {
            return;
        }
        
        double dividerWidth = contentDividers.isEmpty() ? 0 : contentDividers.get(0).prefWidth(-1);

        if (contentDividers.size() > 0 && previousArea != -1 && previousArea != (sw * sh)) {
            //This algorithm adds/subtracts a little to each panel on every resize
            List<Content> resizeList = new ArrayList<Content>();
            for (Content c: contentRegions) {
                if (c.isResizableWithParent()) {
                    resizeList.add(c);
                }
            }
                        
            double delta = horizontal ? (s.getWidth() - previousWidth) : (s.getHeight() - previousHeight);
            boolean growing = delta > 0;
            
            delta = Math.abs(delta);            

            if (!resizeList.isEmpty()) {
                int portion = (int)(delta)/resizeList.size();
                int remainder = (int)delta%resizeList.size();
                int size = 0;
                if (portion == 0) {
                    portion = remainder;
                    size = remainder;
                    remainder = 0;
                } else {
                    size = portion * resizeList.size();
                }

                while (size > 0 && !resizeList.isEmpty()) {
                    if (growing) {
                        lastDividerUpdate++;
                    } else {
                        lastDividerUpdate--;
                        if (lastDividerUpdate < 0) {
                            lastDividerUpdate = contentRegions.size() - 1;
                        }
                    }
                    int id = lastDividerUpdate%contentRegions.size();
                    Content content = contentRegions.get(id);
                    if (content.isResizableWithParent() && resizeList.contains(content)) {
                        double area = content.getArea();
                        if (growing) {
                            double max = horizontal ? content.maxWidth(-1) : content.maxHeight(-1);
                            if ((area + portion) <= max) {
                                area += portion;
                            } else {
                                resizeList.remove(content);
                                continue;
                            }
                        } else {
                            double min = horizontal ? content.minWidth(-1) : content.minHeight(-1);
                            if ((area - portion) >= min) {
                                area -= portion;
                            } else {
                                resizeList.remove(content);
                                continue;
                            }
                        }
                        content.setArea(area);
                        size -= portion;
                        if (size == 0 && remainder != 0) {
                            portion = remainder;
                            size = remainder;
                            remainder = 0;
                        } else if (size == 0) {
                            break;
                        }
                    }
                }
            }

            previousArea = sw * sh;
            previousWidth = sw;
            previousHeight = sh;

            // If we are resizing the window save the current area into
            // resizableWithParentArea.  We use this value during layout.
            for (Content c: contentRegions) {
                c.setResizableWithParentArea(c.getArea());
                c.setAvailable(0);
            }
            resize = true;
        } else {
            previousArea = sw * sh;
            previousWidth = sw;
            previousHeight = sh;
        }
        
        // If the window is less than the min size we want to resize
        // proportionally
        double minSize = totalMinSize();
        if (minSize > (horizontal ? w : h)) {
            double percentage = 0;
            for (int i = 0; i < contentRegions.size(); i++) {
                Content c = contentRegions.get(i);
                double min = horizontal ? c.minWidth(-1) : c.minHeight(-1);
                percentage = min/minSize;
                c.setArea(snapSpace(percentage * (horizontal ? w : h)));
                c.setAvailable(0);
            }
            setupContentAndDividerForLayout();
            layoutDividersAndContent(w, h);
            resize = false;
            return;
        }

        for(int trys = 0; trys < 10; trys++) {
            // Compute the area in between each divider.            
            ContentDivider previousDivider = null;
            ContentDivider divider = null;            
            for (int i = 0; i < contentRegions.size(); i++) {
                double space = 0;                
                if (i < contentDividers.size()) {
                    divider = contentDividers.get(i);
                    if (i == 0) {
                        // First panel
                        space = getAbsoluteDividerPos(divider);
                    } else {
                        // Middle panels
                        if (getAbsoluteDividerPos(divider) <= getAbsoluteDividerPos(previousDivider)) {
                            // The current divider and the previous divider share the same position
                            // or the current divider position is less than the previous position.
                            // We will set the divider next to the previous divider.
                            double pos = getAbsoluteDividerPos(previousDivider);                                
                            checkDividerPos = true;
                            setAbsoluteDividerPos(divider, pos + dividerWidth);
                        }
                        space = getAbsoluteDividerPos(divider) - (getAbsoluteDividerPos(previousDivider) + dividerWidth);
                    }
                } else if (i == contentDividers.size()) {
                    // Last panel
                    space = (horizontal ? w : h) - (previousDivider != null ? getAbsoluteDividerPos(previousDivider) + dividerWidth : 0);
                }
                if (!resize) {
                    contentRegions.get(i).setArea(space);
                }
                previousDivider = divider;
            }

            // Compute the amount of space we have available.
            // Available is amount of space we can take from a panel before we reach its min.
            // If available is negative we don't have enough space and we will
            // proportionally take the space from the other availables.  If we have extra space
            // we will porportionally give it to the others
            double spaceRequested = 0;
            double extraSpace = 0;
            for (Content c: contentRegions) {
                double max = 0;
                double min = 0;
                if (c != null) {
                    max = horizontal ? c.maxWidth(-1) : c.maxHeight(-1);
                    min = horizontal ? c.minWidth(-1) : c.minHeight(-1);
                }

                if (c.getArea() >= max) {
                    // Add the space that needs to be distributed to the others
                    extraSpace += (c.getArea() - max);
                    c.setArea(max);
                }
                c.setAvailable(c.getArea() - min);
                if (c.getAvailable() < 0) {
                    spaceRequested += c.getAvailable();
                }
            }

            spaceRequested = Math.abs(spaceRequested);

            // Add the panels where we can take space from
            List<Content> availableList = new ArrayList<Content>();
            List<Content> storageList = new ArrayList<Content>();
            List<Content> spaceRequestor = new ArrayList<Content>();
            double available = 0;
            for (Content c: contentRegions) {
                if (c.getAvailable() >= 0) {
                    available += c.getAvailable();
                    availableList.add(c);
                }

                if (resize && !c.isResizableWithParent()) {
                    // We are making the SplitPane bigger and will need to
                    // distribute the extra space.
                    if (c.getArea() >= c.getResizableWithParentArea()) {                        
                        extraSpace += (c.getArea() - c.getResizableWithParentArea());
                    } else {
                        // We are making the SplitPane smaller and will need to
                        // distribute the space requested.
                        spaceRequested += (c.getResizableWithParentArea() - c.getArea());
                    }
                    c.setAvailable(0);
                }
                // Add the panels where we can add space to;
                if (resize) {
                    if (c.isResizableWithParent()) {
                        storageList.add(c);
                    }
                } else {
                    storageList.add(c);
                }
                // List of panels that need space.
                if (c.getAvailable() < 0) {
                    spaceRequestor.add(c);
                }
            }

            if (extraSpace > 0) {
                extraSpace = distributeTo(storageList, extraSpace);
                // After distributing add any panels that may still need space to the
                // spaceRequestor list.
                spaceRequested = 0;
                spaceRequestor.clear();
                available = 0;
                availableList.clear();
                for (Content c: contentRegions) {
                    if (c.getAvailable() < 0) {
                        spaceRequested += c.getAvailable();
                        spaceRequestor.add(c);
                    } else {
                        available += c.getAvailable();
                        availableList.add(c);
                    }
                }
                spaceRequested = Math.abs(spaceRequested);
            }

            if (available >= spaceRequested) {
                for (Content requestor: spaceRequestor) {
                    double min = horizontal ? requestor.minWidth(-1) : requestor.minHeight(-1);
                    requestor.setArea(min);
                    requestor.setAvailable(0);
                }
                // After setting all the space requestors to their min we have to
                // redistribute the space requested to any panel that still
                // has available space.
                if (spaceRequested > 0 && !spaceRequestor.isEmpty()) {
                    distributeFrom(spaceRequested, availableList);
                }

                // Only for resizing.  We should have all the panel areas
                // available computed.  We can total them up and see
                // how much space we have left or went over and redistribute.
                if (resize) {
                    double total = 0;
                    for (Content c: contentRegions) {
                        if (c.isResizableWithParent()) {
                            total += c.getArea();
                        } else {
                            total += c.getResizableWithParentArea();
                        }
                    }
                    total += (dividerWidth * contentDividers.size());
                    if (total < (horizontal ? w : h)) {
                        extraSpace += ((horizontal ? w : h) - total);
                        distributeTo(storageList, extraSpace);
                    } else {
                        spaceRequested += (total - (horizontal ? w : h));
                        distributeFrom(spaceRequested, storageList);
                    }
                }
            }

            setupContentAndDividerForLayout();

            // Check the bounds of every panel
            boolean passed = true;
            for (Content c: contentRegions) {
                double max = horizontal ? c.maxWidth(-1) : c.maxHeight(-1);
                double min = horizontal ? c.minWidth(-1) : c.minHeight(-1);
                if (c.getArea() < min || c.getArea() > max) {
                    passed = false;
                    break;
                }
            }
            if (passed) {
                break;
            }
        }

        layoutDividersAndContent(w, h);        
        resize = false;        
    }

    @Override protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minWidth = 0;
        double maxMinWidth = 0;
        for (Content c: contentRegions) {
            minWidth += c.minWidth(-1);
            maxMinWidth = Math.max(maxMinWidth, c.minWidth(-1));
        }
        for (ContentDivider d: contentDividers) {
            minWidth += d.prefWidth(-1);
        }
        if (horizontal) {
            return minWidth + leftInset + rightInset;
        } else {
            return maxMinWidth + leftInset + rightInset;
        }
    }

    @Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minHeight = 0;
        double maxMinHeight = 0;
        for (Content c: contentRegions) {
            minHeight += c.minHeight(-1);
            maxMinHeight = Math.max(maxMinHeight, c.minHeight(-1));
        }
        for (ContentDivider d: contentDividers) {
            minHeight += d.prefWidth(-1);
        }
        if (horizontal) {
            return maxMinHeight + topInset + bottomInset;
        } else {
            return minHeight + topInset + bottomInset;
        }
    }

    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefWidth = 0;
        double prefMaxWidth = 0;
        for (Content c: contentRegions) {
            prefWidth += c.prefWidth(-1);
            prefMaxWidth = Math.max(prefMaxWidth, c.prefWidth(-1));
        }
        for (ContentDivider d: contentDividers) {
            prefWidth += d.prefWidth(-1);
        }
        if (horizontal) {
            return prefWidth + leftInset + rightInset;
        } else {
            return prefMaxWidth + leftInset + rightInset;
        }
    }

    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefHeight = 0;
        double maxPrefHeight = 0;
        for (Content c: contentRegions) {
            prefHeight += c.prefHeight(-1);
            maxPrefHeight = Math.max(maxPrefHeight, c.prefHeight(-1));
        }
        for (ContentDivider d: contentDividers) {
            prefHeight += d.prefWidth(-1);
        }
        if (horizontal) {
            return maxPrefHeight + topInset + bottomInset;
        } else {
            return prefHeight + topInset + bottomInset;
        }
    }
    

//    private void printDividerPositions() {
//        for (int i = 0; i < contentDividers.size(); i++) {
//            System.out.print("DIVIDER[" + i + "] " + contentDividers.get(i).getDividerPos() + " ");
//        } 
//        System.out.println("");
//    }
//    
//    private void printAreaAndAvailable() {
//        for (int i = 0; i < contentRegions.size(); i++) {
//            System.out.print("AREA[" + i + "] " + contentRegions.get(i).getArea() + " ");
//        }
//        System.out.println("");
//        for (int i = 0; i < contentRegions.size(); i++) {
//            System.out.print("AVAILABLE[" + i + "] " + contentRegions.get(i).getAvailable() + " ");
//        }
//        System.out.println("");
//        for (int i = 0; i < contentRegions.size(); i++) {
//            System.out.print("RESIZABLEWTIHPARENT[" + i + "] " + contentRegions.get(i).getResizableWithParentArea() + " ");
//        }
//        System.out.println("");
//    }

    class ContentDivider extends StackPane {
        private double initialPos;
        private double dividerPos;
        private double pressPos;
        private SplitPane.Divider d;
        private StackPane grabber;
        private double x;
        private double y;  
        private ChangeListener<Number> listener;

        public ContentDivider(SplitPane.Divider d) {
            getStyleClass().setAll("split-pane-divider");

            this.d = d;
            this.initialPos = 0;
            this.dividerPos = 0;
            this.pressPos = 0;

            grabber = new StackPane() {
                @Override protected double computeMinWidth(double height) {
                    return 0;
                }

                @Override protected double computeMinHeight(double width) {
                    return 0;
                }

                @Override protected double computePrefWidth(double height) {
                    return snappedLeftInset() + snappedRightInset();
                }

                @Override protected double computePrefHeight(double width) {
                    return snappedTopInset() + snappedBottomInset();
                }

                @Override protected double computeMaxWidth(double height) {
                    return computePrefWidth(-1);
                }

                @Override protected double computeMaxHeight(double width) {
                    return computePrefHeight(-1);
                }
            };
            setGrabberStyle(horizontal);
            getChildren().add(grabber);

            // TODO register a listener for SplitPane.Divider position
        }

        public SplitPane.Divider getDivider() {
            return this.d;
        }

        public final void setGrabberStyle(boolean horizontal) {
            grabber.getStyleClass().clear();
            grabber.getStyleClass().setAll("vertical-grabber");
            setCursor(Cursor.V_RESIZE);
            if (horizontal) {
                grabber.getStyleClass().setAll("horizontal-grabber");
                setCursor(Cursor.H_RESIZE);
            }
        }

        public double getInitialPos() {
            return initialPos;
        }

        public void setInitialPos(double initialPos) {
            this.initialPos = initialPos;
        }

        public double getDividerPos() {
            return dividerPos;
        }

        public void setDividerPos(double dividerPos) {
            this.dividerPos = dividerPos;
        }

        public double getPressPos() {
            return pressPos;
        }

        public void setPressPos(double pressPos) {
            this.pressPos = pressPos;
        }

        // TODO remove x and y and replace with dividerpos.
        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public ChangeListener<Number> getPosPropertyListener() {
            return listener;
        }

        public void setPosPropertyListener(ChangeListener<Number> listener) {
            this.listener = listener;
        }
                
        @Override protected double computeMinWidth(double height) {
            return computePrefWidth(height);
        }

        @Override protected double computeMinHeight(double width) {
            return computePrefHeight(width);
        }

        @Override protected double computePrefWidth(double height) {
            return snappedLeftInset() + snappedRightInset();
        }

        @Override protected double computePrefHeight(double width) {
            return snappedTopInset() + snappedBottomInset();
        }

        @Override protected double computeMaxWidth(double height) {
            return computePrefWidth(height);
        }

        @Override protected double computeMaxHeight(double width) {
            return computePrefHeight(width);
        }

        @Override protected void layoutChildren() {
            double grabberWidth = grabber.prefWidth(-1);
            double grabberHeight = grabber.prefHeight(-1);
            double grabberX = (getWidth() - grabberWidth)/2;
            double grabberY = (getHeight() - grabberHeight)/2;
            grabber.resize(grabberWidth, grabberHeight);
            positionInArea(grabber, grabberX, grabberY, grabberWidth, grabberHeight,
                    /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
        }
    }

    static class Content extends StackPane {
        private Node content;
        private Rectangle clipRect;
        private double x;
        private double y;
        private double area;
        private double resizableWithParentArea;
        private double available;

        public Content(Node n) {
            this.clipRect = new Rectangle();
            setClip(clipRect);
            this.content = n;
            if (n != null) {
                this.setId(n.getId());
                getChildren().add(n);
            }
            this.x = 0;
            this.y = 0;
        }

        public Node getContent() {
            return content;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        // This is the area of the panel.  This will be used as the
        // width/height during layout.
        public double getArea() {
            return area;
        }

        public void setArea(double area) {
            this.area = area;
        }

        // This is the minimum available area for other panels to use
        // if they need more space.
        public double getAvailable() {
            return available;
        }

        public void setAvailable(double available) {
            this.available = available;
        }

        public boolean isResizableWithParent() {
            return SplitPane.isResizableWithParent(content);
        }

        public double getResizableWithParentArea() {
            return resizableWithParentArea;
        }

        // This is used to save the current area during resizing when
        // isResizeableWithParent equals false.
        public void setResizableWithParentArea(double resizableWithParentArea) {
            if (!isResizableWithParent()) {
                this.resizableWithParentArea = resizableWithParentArea;
            } else {
                this.resizableWithParentArea = 0;
            }
        }

        protected void setClipSize(double w, double h) {
            clipRect.setWidth(w);
            clipRect.setHeight(h);
        }

        @Override protected double computeMaxWidth(double height) {
            return snapSize(content.maxWidth(height));
        }

        @Override protected double computeMaxHeight(double width) {
            return snapSize(content.maxHeight(width));
        }
    }
}
