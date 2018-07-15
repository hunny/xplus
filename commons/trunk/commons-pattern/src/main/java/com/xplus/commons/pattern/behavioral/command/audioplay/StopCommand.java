package com.xplus.commons.pattern.behavioral.command.audioplay;

/**
 * 具体命令角色类：停止
 *
 */
public class StopCommand implements Command {

  private AudioPlayer audioPlayer;
  
  public StopCommand(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }
  
  @Override
  public void execute() {
    this.audioPlayer.stop();
  }

}
