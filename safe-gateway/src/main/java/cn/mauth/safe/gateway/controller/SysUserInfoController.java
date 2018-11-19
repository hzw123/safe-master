package cn.mauth.safe.gateway.controller;

import cn.mauth.safe.gateway.dao.SysUserInfoRepository;
import cn.mauth.safe.gateway.domain.SysUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class SysUserInfoController {

    @Autowired
    private SysUserInfoRepository repository;

    @GetMapping
    public List<SysUserInfo> findAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public SysUserInfo findOne(@PathVariable Long id){
        return repository.findById(id).get();
    }

    @PostMapping
    public SysUserInfo saveOne(@RequestBody SysUserInfo sysUserInfo){
        return repository.save(sysUserInfo);
    }

    @PutMapping("/{id}")
    public SysUserInfo updateOne(@PathVariable Long id ,@RequestBody SysUserInfo sysUserInfo){
        if(sysUserInfo.getId()==id){
            return repository.save(sysUserInfo);
        }else{
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable Long id){
        repository.deleteById(id);
    }
}
