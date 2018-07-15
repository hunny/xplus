package com.xplus.commons.pattern.behavioral.command.audioplay;

/**
 * 具体命令角色类：播放
 *
 */
public class PlayCommand implements Command {

  private AudioPlayer audioPlayer;
  
  public PlayCommand(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }
  
  @Override
  public void execute() {
    this.audioPlayer.play();
  }

}
