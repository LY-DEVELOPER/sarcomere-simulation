package com.sarco.sim;

public interface ISimLogic {
	void init() throws Exception;

    void input(Window window);

    void update(float interval);

    void render(Window window);
}
