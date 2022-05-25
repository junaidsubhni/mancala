package com.bol.mancala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MancalaPit implements Serializable {

    private Integer id;
    private Integer stones;

    public void sow () {
        this.stones++;
    }

    public void addStones (Integer stones){
        this.stones+= stones;
    }

    @JsonIgnore
    public Boolean isEmpty (){
        return this.stones == 0;
    }

    public int clear (){
        this.stones = 0;
        return 0;
    }

}
