package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.CommentCreateDto;
import com.rybak.andersenhw3.dto.CommentResponseDto;
import com.rybak.andersenhw3.entity.Comment;

import java.util.List;

public class CommentMapper {

    private UserMapper userMapper;

    public CommentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Comment toComment(CommentCreateDto source) {
        Comment target = new Comment();
        target.setMessage(source.getMessage());

        return target;
    }

    public CommentResponseDto toCommentResponseDto(Comment source) {
        CommentResponseDto target = new CommentResponseDto();
        target.setId(source.getId());
        target.setMessage(source.getMessage());
        target.setUser(userMapper.toUserResponseDto(source.getUser()));

        return target;
    }

    public List<CommentResponseDto> toCommentResponseDtoList(List<Comment> comments) {
        return comments.stream()
                .map(this::toCommentResponseDto)
                .toList();
    }

}
