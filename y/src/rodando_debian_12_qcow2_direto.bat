:: super leve
:: liga em 11 segundos.

:: https://saimei.ftp.acc.umu.se/images/cloud/bookworm/latest/debian-12-generic-amd64.qcow2
:: https://cdn.amazonlinux.com/os-images/2.0.20260120.1/seed.iso
:: ou https://github.com/ywanes/utility_y/raw/refs/heads/master/y/utils_exe/seed.iso
:: aqui contem os instaladores da dependencia hax: https://github.com/ywanes/utility_y/blob/master/y/src/a.ps1
:: espaço
:: C:\qemu\qemu-img.exe info C:\qemu_hax2\debian-12-generic-amd64.qcow2
:: expandindo espaço
:: C:\qemu\qemu-img.exe resize C:\qemu_hax2\debian-12-generic-amd64.qcow2 +10G
:: user:user1 senha: amazon

C:\qemu\qemu-system-x86_64.exe -L C:\qemu_share -m 4G -smp 2,cores=2,threads=1,sockets=1 -accel hax -cpu Westmere -drive file=C:\qemu_hax2\debian-12-generic-amd64.qcow2,if=virtio -drive file=C:\qemu_hax2\seed.iso,if=virtio,format=raw,readonly=on -netdev user,id=net0,hostfwd=tcp::2222-:22 -device virtio-net-pci,netdev=net0 -nographic -serial mon:stdio -rtc base=localtime,clock=host
