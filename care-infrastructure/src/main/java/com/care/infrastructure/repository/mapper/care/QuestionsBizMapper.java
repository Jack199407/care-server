package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.Questions;

public interface QuestionsBizMapper {
    Questions selectByPrimaryKey(Integer id);
}