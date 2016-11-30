package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
	 TriangleMain caller;
	   
	 public MyInputProcessor(TriangleMain caller){
		 this.caller = caller;
	 }
	   
	 @Override
	 public boolean touchDown (int x, int y, int pointer, int button) {
	    if (button == Input.Buttons.LEFT) {
	    	caller.leftClick(x, y);
	        return true;     
	    }
	    return false;
	 }

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT){
			caller.setMulti(true);
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.ENTER){
			caller.process();
			return true;
		}
		if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT){
			caller.setMulti(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		caller.scroll(amount);
		return false;
	}
	}