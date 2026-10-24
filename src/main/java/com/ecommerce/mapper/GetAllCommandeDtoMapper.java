package com.ecommerce.mapper;

import com.ecommerce.dto.CommandResponseDto;
import com.ecommerce.entities.Commande;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@Component
public class GetAllCommandeDtoMapper implements Function<List<Commande>,List<CommandResponseDto>> {
   private  final  CommandeDtoMapper commandeDtoMapper;

    public GetAllCommandeDtoMapper(CommandeDtoMapper commandeDtoMapper) {
        this.commandeDtoMapper = commandeDtoMapper;
    }

    @Override
    public List<CommandResponseDto> apply(List<Commande> commandes) {
        List<CommandResponseDto> commandResponseDtos=new ArrayList<>();
        for (Commande commande:commandes){
            // INITIALISATION DES CommandResponseDto A TRAVERS LA commandeDtoMapper FUNCTION
           CommandResponseDto commandeDtoMapper = this.commandeDtoMapper.apply(commande);
            commandResponseDtos.add(commandeDtoMapper);
        }
        return commandResponseDtos;
    }
}
